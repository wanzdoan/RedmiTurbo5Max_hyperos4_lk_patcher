// ffff00009072fe8c  size=260  FUN_ffff00009072fe8c

undefined4 FUN_ffff00009072fe8c(undefined4 param_1,undefined8 *param_2,uint param_3)

{
  uint *puVar1;
  char *pcVar2;
  undefined8 uVar3;
  uint uVar4;
  undefined4 uVar5;
  
  puVar1 = (uint *)FUN_ffff000090730e64(0,param_1);
  if (puVar1 == (uint *)0x0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_Not_found_relate_me_ffff0000907fb4d6,
               s_get_sub_img_blk_inf_ffff0000907f1190,0x181,param_1);
    uVar5 = 0;
  }
  else {
    uVar4 = *puVar1;
    if (uVar4 == 0) {
      FUN_ffff00009077de54
                (s__s__d___CCCI_attr_flag_0x_x_Offs_ffff0000907f7fa7,
                 s_get_sub_img_blk_inf_ffff0000907f1190,0x185,param_1);
      uVar5 = 0xffffffff;
    }
    else {
      if (uVar4 < param_3) {
        if (puVar1[1] + uVar4 <= param_3) {
          *param_2 = puVar1;
          return 1;
        }
        param_3 = param_3 - uVar4;
        pcVar2 = s__s__d___CCCI_attr_flag_0x_x_Memo_ffff0000907f11a4;
        uVar3 = 399;
        uVar4 = puVar1[1];
      }
      else {
        pcVar2 = s__s__d___CCCI_attr_flag_0x_x_Get_i_ffff0000907fd17b;
        uVar3 = 0x189;
      }
      FUN_ffff00009077de54
                (pcVar2,s_get_sub_img_blk_inf_ffff0000907f1190,uVar3,param_1,uVar4,param_3);
      uVar5 = 0xffffffff;
    }
  }
  return uVar5;
}


