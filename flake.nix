# if you can make it not use fhs PLEASE pr

{
	description = "bfkt devShell";

	inputs = {
		nixpkgs.url = "https://channels.nixos.org/nixpkgs-unstable/nixexprs.tar.zst";
		kotlin-lsp = {
			url = "github:itsyunaya/kotlin-lsp-nix";
			inputs.nixpkgs.follows = "nixpkgs";
		};
	};

	outputs = { self, nixpkgs, kotlin-lsp }: let
		systems = [ "x86_64-linux" "aarch64-linux" "aarch64-darwin" ];
		forAllSystems = f: nixpkgs.lib.genAttrs systems (system: f nixpkgs.legacyPackages.${system});
	in {
		devShells = forAllSystems (pkgs: let
			sys = pkgs.stdenv.hostPlatform.system;

			# me when unfree packages
			lsp = kotlin-lsp.packages.${sys}.default.overrideAttrs (finalAttrs: prevAttrs: {
				meta = prevAttrs.meta // { license = []; };
			});

			tools = [ pkgs.kotlin-cli lsp ];
		in {
			default = if pkgs.stdenv.isLinux
			then
				# fhs env because kotlin-toolchain is STUPID and EVIL and HATES NIXOS
				(pkgs.buildFHSEnv {
					name = "kotlin-dev";
					targetPkgs = p:
						tools
						++ (with p; [
							zlib
							ncurses
							libxml2
							stdenv.cc.cc.lib
							libxcrypt-legacy # provides old ver of libcrypt because some binaries need it ???
						]);
					runScript = "bash";
				}).env
			else pkgs.mkShell { packages = tools; };
		});
	};
}
