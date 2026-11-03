// ffff000090771d58  size=196  FUN_ffff000090771d58

long FUN_ffff000090771d58(long param_1,ulong param_2)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  
  if (param_2 < 0x28) {
    FUN_ffff00009076fc00(s_ERROR___s____ffff0000907f352e,s_ufdt_install_blob_ffff0000907e6aa6);
    FUN_ffff00009076fc00(s_Blob_size__zu_smaller_than_the_h_ffff0000907f531e,param_2,0x28);
  }
  else {
    iVar2 = FUN_ffff00009076c880();
    if (-1 < iVar2) {
      return param_1;
    }
    FUN_ffff00009076fc00(s_ERROR___s____ffff0000907f352e,s_ufdt_install_blob_ffff0000907e6aa6);
    if (iVar2 == -10) {
      uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
      FUN_ffff00009076fc00
                (s_incompatible_blob_version___d__s_ffff0000907eeae7,uVar1 >> 0x10 | uVar1 << 0x10,
                 0x11);
    }
    else {
      uVar3 = FUN_ffff00009076ee00(iVar2);
      FUN_ffff00009076fc00(s_error_validating_blob___s_ffff0000907ffb4a,uVar3);
    }
  }
  return 0;
}


