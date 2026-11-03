
undefined4
FUN_ffff0000be091ff8(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  FUN_ffff0000be05fd98
            (s___s__enter_seclib_write_image__e_ffff0000be0beffb,s_SEC_CFG_ffff0000be0c6d63,0);
  lVar2 = FUN_ffff0000be05df6c(param_1);
  if (lVar2 == 0) {
    uVar3 = 0x100008;
    FUN_ffff0000be05fd98
              (s___s__Partition___s__is_not_exist_ffff0000be0bb872,&DAT_ffff0000be0bb811,param_1);
  }
  else {
    iVar1 = FUN_ffff0000be05e088(lVar2,param_2,param_3,(long)param_4);
    if (iVar1 < param_4) {
      uVar3 = 0x100008;
      FUN_ffff0000be05fd98
                (s___s__Partition___s__write_fail__o_ffff0000be0b17d6,&DAT_ffff0000be0bb811,param_1,
                 param_3,param_4);
    }
    else {
      uVar3 = 0;
    }
  }
  FUN_ffff0000be05fd98
            (s___s__reach_seclib_write_image_en_ffff0000be0ae1c4,s_SEC_CFG_ffff0000be0c6d63,uVar3);
  return uVar3;
}


