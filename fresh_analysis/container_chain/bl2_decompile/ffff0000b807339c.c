
undefined4
FUN_ffff0000b807339c(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  FUN_ffff0000b804c33c
            (s___s__enter_seclib_write_image__e_ffff0000b809b27b,s_SEC_CFG_ffff0000b80a18d5,0);
  lVar2 = FUN_ffff0000b804a99c(param_1);
  if (lVar2 == 0) {
    uVar3 = 0x100008;
    FUN_ffff0000b804c33c
              (s___s__Partition___s__is_not_exist_ffff0000b8098847,&DAT_ffff0000b80987e6,param_1);
  }
  else {
    iVar1 = FUN_ffff0000b804aab8(lVar2,param_2,param_3,(long)param_4);
    if (iVar1 < param_4) {
      uVar3 = 0x100008;
      FUN_ffff0000b804c33c
                (s___s__Partition___s__write_fail__o_ffff0000b80905c2,&DAT_ffff0000b80987e6,param_1,
                 param_3,param_4);
    }
    else {
      uVar3 = 0;
    }
  }
  FUN_ffff0000b804c33c
            (s___s__reach_seclib_write_image_en_ffff0000b808d9b8,s_SEC_CFG_ffff0000b80a18d5,uVar3);
  return uVar3;
}


