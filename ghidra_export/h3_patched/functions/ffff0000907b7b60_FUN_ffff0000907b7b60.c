// ffff0000907b7b60  size=228  FUN_ffff0000907b7b60

undefined4
FUN_ffff0000907b7b60(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  FUN_ffff00009077de54
            (s___s__enter_seclib_write_image__e_ffff0000907f74ea,s_SEC_CFG_ffff000090803332,0);
  lVar2 = FUN_ffff00009077bd60(param_1);
  if (lVar2 == 0) {
    uVar3 = 0x100008;
    FUN_ffff00009077de54
              (s___s__Partition___s__is_not_exist_ffff0000907f2148,&DAT_ffff0000907f20e7,param_1);
  }
  else {
    iVar1 = FUN_ffff00009077be7c(lVar2,param_2,param_3,(long)param_4);
    if (iVar1 < param_4) {
      uVar3 = 0x100008;
      FUN_ffff00009077de54
                (s___s__Partition___s__write_fail__o_ffff0000907e3d76,&DAT_ffff0000907f20e7,param_1,
                 param_3,param_4);
    }
    else {
      uVar3 = 0;
    }
  }
  FUN_ffff00009077de54
            (s___s__reach_seclib_write_image_en_ffff0000907df027,s_SEC_CFG_ffff000090803332,uVar3);
  return uVar3;
}


