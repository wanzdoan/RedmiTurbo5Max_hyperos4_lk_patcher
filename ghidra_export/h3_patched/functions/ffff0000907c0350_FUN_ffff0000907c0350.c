// ffff0000907c0350  size=828  FUN_ffff0000907c0350

int FUN_ffff0000907c0350
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
    uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar6 = &DAT_ffff0000907d0759;
    pcVar7 = s___Persistent_values_are_not_impl_ffff0000907e07a2;
  }
  else {
    lVar4 = FUN_ffff0000907c14dc(s_avb_persistent_digest__ffff0000907fe5f3,param_2,0);
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
            iVar3 = FUN_ffff0000907c0350(param_1,param_2,param_3,0,param_5);
            if (iVar3 != 0) {
              uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
              FUN_ffff0000907c0874
                        (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907e895e,
                         s___ERROR__ffff0000907e0722,param_2,
                         s___Reading_back_initialized_persi_ffff0000907da2a2,0);
            }
          }
          else {
            iVar3 = 1;
            if (iVar2 != 1) {
              uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
              FUN_ffff0000907c0874
                        (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907dd672,
                         s___ERROR__ffff0000907e0722,param_2,
                         s___Error_initializing_persistent_d_ffff0000907d075d,0);
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
          uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          FUN_ffff0000907c0874
                    (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907ea34d,s___ERROR__ffff0000907e0722,
                     s_Error_getting_device_lock_state__ffff0000907f095d,0);
          iVar3 = 2;
        }
      }
      FUN_ffff0000907c09a0(lVar4);
      return iVar3;
    }
    FUN_ffff0000907c09a0(lVar4);
    if (uVar1 == 1) {
      return 1;
    }
    if (uVar1 == 5) {
      uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      FUN_ffff0000907c0874
                (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907f0959,s___ERROR__ffff0000907e0722,
                 param_2,s___Persistent_digest_does_not_exi_ffff0000907d5523,0);
      return 3;
    }
    if ((uVar1 & 0xfffffffe) == 6) {
      uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      puVar6 = &DAT_ffff0000907dbc7b;
    }
    else {
      if (uVar1 != 0) {
        uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        FUN_ffff0000907c0874
                  (uVar5,&DAT_ffff0000907ebe0c,&DAT_ffff0000907e07cc,s___ERROR__ffff0000907e0722,
                   param_2,s___Error_reading_persistent_diges_ffff0000907d70a1,0);
        return 2;
      }
      if (local_58 == param_3) {
        return 0;
      }
      uVar5 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      puVar6 = (undefined *)0xffff0000907ef096;
    }
    pcVar7 = s___Persistent_digest_is_not_of_ex_ffff000090801a42;
  }
  FUN_ffff0000907c0874
            (uVar5,&DAT_ffff0000907ebe0c,puVar6,s___ERROR__ffff0000907e0722,param_2,pcVar7,0);
  return 6;
}


