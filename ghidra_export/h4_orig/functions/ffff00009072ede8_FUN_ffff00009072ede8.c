// ffff00009072ede8  size=72  FUN_ffff00009072ede8

ulong FUN_ffff00009072ede8(undefined8 param_1,undefined4 param_2)

{
  int iVar1;
  ulong uVar2;
  
  uVar2 = FUN_ffff0000907324a4(s_md_build_ver_ffff0000907f47d5,param_1,param_2);
  if ((int)uVar2 != 0) {
    iVar1 = FUN_ffff00009077f250(param_1);
    uVar2 = (ulong)(iVar1 + 1);
  }
  return uVar2;
}


