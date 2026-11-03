
undefined8
FUN_ffff0000907c2afc
          (long param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,undefined8 *param_5
          )

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  undefined *puVar4;
  char *pcVar5;
  char *pcVar6;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined4 local_70;
  undefined4 uStack_6c;
  undefined4 uStack_68;
  undefined4 uStack_64;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_28;
  
  uVar2 = thunk_FUN_ffff00009077f250(param_2);
  uVar3 = FUN_ffff0000907c1384(param_4,0x20,s_vbmeta_ffff0000907da540,6,param_2,uVar2);
  if ((uVar3 & 1) == 0) {
    uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_user_avb_use_ffff0000907d07cc);
    puVar4 = &DAT_ffff0000907f3d11;
LAB_ffff0000907c2c30:
    FUN_ffff0000907c0ee8
              (uVar2,&DAT_ffff0000907ebfa6,puVar4,s___ERROR__ffff0000907e07dc,
               s_Partition_name_and_suffix_does_n_ffff0000908035e2,0);
  }
  else {
    local_28 = 0;
    iVar1 = (**(code **)(param_1 + 0x18))(param_1,param_4,0,0x100,param_3,&local_28);
    if (iVar1 == 3) {
      local_50 = 0;
      uStack_48 = 0;
      local_60 = 0;
      uStack_58 = 0;
      local_70 = 0;
      uStack_6c = 0;
      uStack_68 = 0;
      uStack_64 = 0;
      local_80 = 0;
      uStack_78 = 0;
      uVar2 = thunk_FUN_ffff00009077f250(param_2);
      uVar3 = FUN_ffff0000907c1384(param_4,0x20,&DAT_ffff0000907d0a72,4,param_2,uVar2);
      if ((uVar3 & 1) == 0) {
        uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_user_avb_use_ffff0000907d07cc);
        puVar4 = &DAT_ffff0000907ed90f;
        goto LAB_ffff0000907c2c30;
      }
      iVar1 = (**(code **)(param_1 + 0x18))
                        (param_1,param_4,0xffffffffffffffc0,0x40,&local_80,&local_28);
      if (iVar1 != 0) {
        uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_user_avb_use_ffff0000907d07cc);
        puVar4 = &DAT_ffff0000907f91e2;
        pcVar5 = s_Error_loading_footer_from_partit_ffff0000907f0b3c;
        goto LAB_ffff0000907c2c84;
      }
      iVar1 = thunk_FUN_ffff00009077eae8(&local_80,&DAT_ffff0000907f76fd,4);
      if (iVar1 == 0) {
        uVar2 = FUN_ffff0000907c1060(CONCAT44(uStack_68,uStack_6c));
        iVar1 = (**(code **)(param_1 + 0x18))(param_1,param_4,uVar2,0x100,param_3,&local_28);
        goto joined_r0xffff0000907c2c54;
      }
      uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_user_avb_use_ffff0000907d07cc);
      puVar4 = &DAT_ffff0000907fe7e5;
      pcVar5 = s_Data_from___ffff0000907e3f31;
      pcVar6 = s___does_not_look_like_a_vbmeta_fo_ffff0000907ebd06;
    }
    else {
      uVar2 = 0;
joined_r0xffff0000907c2c54:
      if (iVar1 == 0) {
        if (param_5 != (undefined8 *)0x0) {
          *param_5 = uVar2;
          return 1;
        }
        return 1;
      }
      uVar2 = FUN_ffff0000907c11dc(s_external_lib_libavb_user_avb_use_ffff0000907d07cc);
      puVar4 = &DAT_ffff0000907d0774;
      pcVar5 = s_Error_loading_from_partition___ffff0000907ea4b5;
LAB_ffff0000907c2c84:
      pcVar6 = &DAT_ffff0000907dd78c;
    }
    FUN_ffff0000907c0ee8
              (uVar2,&DAT_ffff0000907ebfa6,puVar4,s___ERROR__ffff0000907e07dc,pcVar5,param_4,pcVar6,
               0);
  }
  return 0;
}


