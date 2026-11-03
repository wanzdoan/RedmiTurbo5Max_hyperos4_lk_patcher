// ffff0000907b81d4  size=228  FUN_ffff0000907b81d4

undefined4
FUN_ffff0000907b81d4(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  FUN_ffff00009077e11c
            (s___s__enter_seclib_write_image__e_ffff0000907f7674,s_SEC_CFG_ffff00009080355a,0);
  lVar2 = FUN_ffff00009077c028(param_1);
  if (lVar2 == 0) {
    uVar3 = 0x100008;
    FUN_ffff00009077e11c
              (s___s__Partition___s__is_not_exist_ffff0000907f22eb,&DAT_ffff0000907f228a,param_1);
  }
  else {
    iVar1 = FUN_ffff00009077c144(lVar2,param_2,param_3,(long)param_4);
    if (iVar1 < param_4) {
      uVar3 = 0x100008;
      FUN_ffff00009077e11c
                (s___s__Partition___s__write_fail__o_ffff0000907e3e5e,&DAT_ffff0000907f228a,param_1,
                 param_3,param_4);
    }
    else {
      uVar3 = 0;
    }
  }
  FUN_ffff00009077e11c
            (s___s__reach_seclib_write_image_en_ffff0000907df0e1,s_SEC_CFG_ffff00009080355a,uVar3);
  return uVar3;
}


