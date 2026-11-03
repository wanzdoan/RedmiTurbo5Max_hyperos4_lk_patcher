
long FUN_ffff0000b8079928(long param_1,ulong param_2)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  
  if (param_2 < 0x28) {
    FUN_ffff0000b80777d0(s_ERROR___s____ffff0000b809976e,s_ufdt_install_blob_ffff0000b8092175);
    FUN_ffff0000b80777d0(s_Blob_size__zu_smaller_than_the_h_ffff0000b809a549,param_2,0x28);
  }
  else {
    iVar2 = FUN_ffff0000b80470b4();
    if (-1 < iVar2) {
      return param_1;
    }
    FUN_ffff0000b80777d0(s_ERROR___s____ffff0000b809976e,s_ufdt_install_blob_ffff0000b8092175);
    if (iVar2 == -10) {
      uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
      FUN_ffff0000b80777d0
                (s_incompatible_blob_version___d__s_ffff0000b8096ce5,uVar1 >> 0x10 | uVar1 << 0x10,
                 0x11);
    }
    else {
      uVar3 = FUN_ffff0000b8048c30(iVar2);
      FUN_ffff0000b80777d0(s_error_validating_blob___s_ffff0000b809ff82,uVar3);
    }
  }
  return 0;
}


