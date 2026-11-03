
void FUN_ffff00009070cd38(undefined8 param_1)

{
  int iVar1;
  ulong uVar2;
  
  if ((DAT_ffff000090816d70 == '\0' || DAT_ffff00009083dfc8 == 0) ||
     ((iVar1 = FUN_ffff00009077f394(s_system_ffff0000907edad2,&DAT_ffff00009083d3d8,0x20),
      iVar1 != 0 &&
      (iVar1 = FUN_ffff00009077f394(s_super_ffff0000907e5b8c,&DAT_ffff00009083d3d8,0x20), iVar1 != 0
      )))) {
    uVar2 = FUN_ffff00009070e30c(param_1);
  }
  else {
    uVar2 = FUN_ffff00009070e1b8(param_1);
  }
  if ((uVar2 & 1) != 0) {
    return;
  }
  if (DAT_ffff00009083dfc0 == 0) {
    DAT_ffff00009083dfc0 = FUN_ffff000090709ab8(s_max_download_size_ffff0000907f7b46);
  }
  FUN_ffff000090709a58(s_max_download_size_ffff0000907f7b46,DAT_ffff00009083dfc0);
  return;
}


