
long FUN_ffff0000be0987a4(long param_1,ulong param_2)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  
  if (param_2 < 0x28) {
    FUN_ffff0000be09664c(s_ERROR___s____ffff0000be0bcb11,s_ufdt_install_blob_ffff0000be0b39c0);
    FUN_ffff0000be09664c(s_Blob_size__zu_smaller_than_the_h_ffff0000be0bde12,param_2,0x28);
  }
  else {
    iVar2 = FUN_ffff0000be055460();
    if (-1 < iVar2) {
      return param_1;
    }
    FUN_ffff0000be09664c(s_ERROR___s____ffff0000be0bcb11,s_ufdt_install_blob_ffff0000be0b39c0);
    if (iVar2 == -10) {
      uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
      FUN_ffff0000be09664c
                (s_incompatible_blob_version___d__s_ffff0000be0b957f,uVar1 >> 0x10 | uVar1 << 0x10,
                 0x11);
    }
    else {
      uVar3 = FUN_ffff0000be056c58(iVar2);
      FUN_ffff0000be09664c(s_error_validating_blob___s_ffff0000be0c4b26,uVar3);
    }
  }
  return 0;
}


