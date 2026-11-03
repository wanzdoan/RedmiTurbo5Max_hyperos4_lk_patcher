// ffff000090730124  size=260  FUN_ffff000090730124

undefined4 FUN_ffff000090730124(undefined4 param_1,undefined8 *param_2,uint param_3)

{
  uint *puVar1;
  char *pcVar2;
  undefined8 uVar3;
  uint uVar4;
  undefined4 uVar5;
  
  puVar1 = (uint *)FUN_ffff0000907310fc(0,param_1);
  if (puVar1 == (uint *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_Not_found_relate_me_ffff0000907fb672,
               s_get_sub_img_blk_inf_ffff0000907f1301,0x181,param_1);
    uVar5 = 0;
  }
  else {
    uVar4 = *puVar1;
    if (uVar4 == 0) {
      FUN_ffff00009077e11c
                (s__s__d___CCCI_attr_flag_0x_x_Offs_ffff0000907f8143,
                 s_get_sub_img_blk_inf_ffff0000907f1301,0x185,param_1);
      uVar5 = 0xffffffff;
    }
    else {
      if (uVar4 < param_3) {
        if (puVar1[1] + uVar4 <= param_3) {
          *param_2 = puVar1;
          return 1;
        }
        param_3 = param_3 - uVar4;
        pcVar2 = s__s__d___CCCI_attr_flag_0x_x_Memo_ffff0000907f1315;
        uVar3 = 399;
        uVar4 = puVar1[1];
      }
      else {
        pcVar2 = s__s__d___CCCI_attr_flag_0x_x_Get_i_ffff0000907fd323;
        uVar3 = 0x189;
      }
      FUN_ffff00009077e11c
                (pcVar2,s_get_sub_img_blk_inf_ffff0000907f1301,uVar3,param_1,uVar4,param_3);
      uVar5 = 0xffffffff;
    }
  }
  return uVar5;
}


