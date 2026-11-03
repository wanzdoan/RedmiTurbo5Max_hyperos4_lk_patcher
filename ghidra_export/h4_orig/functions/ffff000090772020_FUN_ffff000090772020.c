// ffff000090772020  size=196  FUN_ffff000090772020

long FUN_ffff000090772020(long param_1,ulong param_2)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  
  if (param_2 < 0x28) {
    FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_install_blob_ffff0000907e6bba);
    FUN_ffff00009076fec8(s_Blob_size__zu_smaller_than_the_h_ffff0000907f548b,param_2,0x28);
  }
  else {
    iVar2 = FUN_ffff00009076cb48();
    if (-1 < iVar2) {
      return param_1;
    }
    FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_install_blob_ffff0000907e6bba);
    if (iVar2 == -10) {
      uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
      FUN_ffff00009076fec8
                (s_incompatible_blob_version___d__s_ffff0000907eec58,uVar1 >> 0x10 | uVar1 << 0x10,
                 0x11);
    }
    else {
      uVar3 = FUN_ffff00009076f0c8(iVar2);
      FUN_ffff00009076fec8(s_error_validating_blob___s_ffff0000907ffcf2,uVar3);
    }
  }
  return 0;
}


