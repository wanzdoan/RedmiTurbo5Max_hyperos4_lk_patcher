
undefined8
FUN_ffff0000be096d24
          (long param_1,undefined8 param_2,undefined4 *param_3,int param_4,undefined8 param_5)

{
  undefined4 uVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  if (param_1 != 0) {
    if (param_4 == 1) {
      uVar3 = FUN_ffff0000be0555b8(param_1,param_2,4);
      uVar3 = FUN_ffff0000be09732c(param_1,uVar3,param_5);
      do {
        uVar1 = *param_3;
        iVar2 = FUN_ffff0000be055660(param_1,uVar1,param_3);
        if (iVar2 == 9) {
          FUN_ffff0000be09664c(s_ERROR___s____ffff0000be0bcb11,s_fdt_to_ufdt_tree_ffff0000be0a66e6);
          FUN_ffff0000be09664c(s_failed_to_get_next_tag_ffff0000be0b39a8);
          return uVar3;
        }
        uVar4 = FUN_ffff0000be096d24(param_1,uVar1,param_3,iVar2,param_5);
        FUN_ffff0000be09745c(uVar3,uVar4);
      } while (iVar2 != 2);
      return uVar3;
    }
    if (param_4 == 3) {
      uVar3 = FUN_ffff0000be0555b8(param_1,param_2,4);
      uVar3 = FUN_ffff0000be09732c(param_1,uVar3,param_5);
      return uVar3;
    }
  }
  return 0;
}


