// ffff00009070ccc0  size=200  FUN_ffff00009070ccc0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070ccc0(undefined8 param_1)

{
  int iVar1;
  ulong uVar2;
  
  if ((DAT_ffff000090816d60 == '\0' || _DAT_ffff00009083df90 == 0) ||
     ((iVar1 = FUN_ffff00009077f0cc(s_system_ffff0000907ed94a,0xffff00009083d39c,0x20), iVar1 != 0
      && (iVar1 = FUN_ffff00009077f0cc(s_super_ffff0000907e5a61,0xffff00009083d39c,0x20), iVar1 != 0
         )))) {
    uVar2 = FUN_ffff00009070e294(param_1);
  }
  else {
    uVar2 = FUN_ffff00009070e140(param_1);
  }
  if ((uVar2 & 1) != 0) {
    return;
  }
  if (_DAT_ffff00009083df88 == 0) {
    _DAT_ffff00009083df88 = FUN_ffff000090709b2c(s_max_download_size_ffff0000907f79bc);
  }
  FUN_ffff000090709acc(s_max_download_size_ffff0000907f79bc,_DAT_ffff00009083df88);
  return;
}


