// ffff00009070cd38  size=200  FUN_ffff00009070cd38

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070cd38(undefined8 param_1)

{
  int iVar1;
  ulong uVar2;
  
  if ((DAT_ffff000090816d70 == '\0' || _DAT_ffff00009083dfc8 == 0) ||
     ((iVar1 = FUN_ffff00009077f394(s_system_ffff0000907edad2,0xffff00009083d3d8,0x20), iVar1 != 0
      && (iVar1 = FUN_ffff00009077f394(s_super_ffff0000907e5b8c,0xffff00009083d3d8,0x20), iVar1 != 0
         )))) {
    uVar2 = FUN_ffff00009070e30c(param_1);
  }
  else {
    uVar2 = FUN_ffff00009070e1b8(param_1);
  }
  if ((uVar2 & 1) != 0) {
    return;
  }
  if (_DAT_ffff00009083dfc0 == 0) {
    _DAT_ffff00009083dfc0 = FUN_ffff000090709ab8(s_max_download_size_ffff0000907f7b46);
  }
  FUN_ffff000090709a58(s_max_download_size_ffff0000907f7b46,_DAT_ffff00009083dfc0);
  return;
}


