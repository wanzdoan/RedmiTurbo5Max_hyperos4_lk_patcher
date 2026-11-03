
int FUN_ffff0000907c09c4
              (long param_1,undefined8 param_2,long param_3,long param_4,undefined8 param_5)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  undefined8 uVar5;
  undefined *puVar6;
  char *pcVar7;
  long local_58;
  char local_34 [4];
  
  local_58 = 0;
  if (*(long *)(param_1 + 0x60) == 0) {
    uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
    puVar6 = &DAT_ffff0000907d0778;
    pcVar7 = s___Persistent_values_are_not_impl_ffff0000907e085c;
  }
  else {
    lVar4 = FUN_ffff0000907c1b50(s_avb_persistent_digest__ffff0000907fe79b,param_2,0);
    if (lVar4 == 0) {
      return 1;
    }
    uVar1 = (**(code **)(param_1 + 0x60))(param_1,lVar4,param_3,param_5,&local_58);
    if ((param_4 != 0) && (uVar1 == 5)) {
      local_34[0] = '\x01';
      iVar2 = (**(code **)(param_1 + 0x48))(param_1,local_34);
      if (iVar2 == 0) {
        if (local_34[0] == '\0') {
          iVar2 = (**(code **)(param_1 + 0x68))(param_1,lVar4,param_3,param_4);
          if (iVar2 == 0) {
            iVar3 = FUN_ffff0000907c09c4(param_1,param_2,param_3,0,param_5);
            if (iVar3 != 0) {
              uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
              FUN_ffff0000907c0ee8
                        (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e8ab9,
                         s___ERROR__ffff0000907e07dc,param_2,
                         s___Reading_back_initialized_persi_ffff0000907da350,0);
            }
          }
          else {
            iVar3 = 1;
            if (iVar2 != 1) {
              uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
              FUN_ffff0000907c0ee8
                        (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907dd72c,
                         s___ERROR__ffff0000907e07dc,param_2,
                         s___Error_initializing_persistent_d_ffff0000907d077c,0);
              iVar3 = 2;
            }
          }
        }
        else {
          iVar3 = 3;
        }
      }
      else {
        iVar3 = 1;
        if (iVar2 != 1) {
          uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
          FUN_ffff0000907c0ee8
                    (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907ea4a8,s___ERROR__ffff0000907e07dc,
                     s_Error_getting_device_lock_state__ffff0000907f0ae5,0);
          iVar3 = 2;
        }
      }
      FUN_ffff0000907c1014(lVar4);
      return iVar3;
    }
    FUN_ffff0000907c1014(lVar4);
    if (uVar1 == 1) {
      return 1;
    }
    if (uVar1 == 5) {
      uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      FUN_ffff0000907c0ee8
                (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907f0ae1,s___ERROR__ffff0000907e07dc,
                 param_2,s___Persistent_digest_does_not_exi_ffff0000907d5560,0);
      return 3;
    }
    if ((uVar1 & 0xfffffffe) == 6) {
      uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      puVar6 = &DAT_ffff0000907dbd9d;
    }
    else {
      if (uVar1 != 0) {
        uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
        FUN_ffff0000907c0ee8
                  (uVar5,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e0886,s___ERROR__ffff0000907e07dc,
                   param_2,s___Error_reading_persistent_diges_ffff0000907d7137,0);
        return 2;
      }
      if (local_58 == param_3) {
        return 0;
      }
      uVar5 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      puVar6 = (undefined *)0xffff0000907ef21e;
    }
    pcVar7 = s___Persistent_digest_is_not_of_ex_ffff000090801c1e;
  }
  FUN_ffff0000907c0ee8
            (uVar5,&DAT_ffff0000907ebfa6,puVar6,s___ERROR__ffff0000907e07dc,param_2,pcVar7,0);
  return 6;
}


