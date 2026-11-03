// ffff0000907bf5d4  size=800  FUN_ffff0000907bf5d4

/* WARNING: Type propagation algorithm not settling */

int FUN_ffff0000907bf5d4(long param_1,long param_2,undefined4 *param_3)

{
  long lVar1;
  int iVar2;
  undefined8 uVar3;
  undefined *puVar4;
  char *pcVar5;
  undefined4 uVar6;
  long lVar7;
  ulong uVar8;
  long local_158 [35];
  
  local_158[7] = 0;
  local_158[8] = 0;
  local_158[5] = 0;
  local_158[6] = 0;
  local_158[3] = 0;
  local_158[4] = 0;
  local_158[1] = 0;
  local_158[2] = 0;
  local_158[0] = 0;
  iVar2 = (**(code **)(param_1 + 0x60))
                    (param_1,s_avb_managed_verity_mode_ffff0000907ecf30,0x20,local_158 + 1,local_158
                    );
  if (iVar2 == 5) {
LAB_ffff0000907bf644:
    iVar2 = FUN_ffff000090766824();
    if (iVar2 == 0) {
      iVar2 = 0;
      uVar6 = 1;
      goto LAB_ffff0000907bf8d0;
    }
    local_158[0x21] = 0;
    local_158[0x22] = 0;
    local_158[0x1f] = 0;
    local_158[0x20] = 0;
    local_158[0x1d] = 0;
    local_158[0x1e] = 0;
    local_158[0x1b] = 0;
    local_158[0x1c] = 0;
    local_158[0x19] = 0;
    local_158[0x1a] = 0;
    local_158[0x17] = 0;
    local_158[0x18] = 0;
    local_158[0x15] = 0;
    local_158[0x16] = 0;
    local_158[0x13] = 0;
    local_158[0x14] = 0;
    local_158[0x11] = 0;
    local_158[0x12] = 0;
    local_158[0xf] = 0;
    local_158[0x10] = 0;
    local_158[0xd] = 0;
    local_158[0xe] = 0;
    local_158[0xb] = 0;
    local_158[0xc] = 0;
    local_158[9] = 0;
    local_158[10] = 0;
    FUN_ffff0000907c6564(local_158 + 9);
    if (*(long *)(param_2 + 0x10) != 0) {
      lVar7 = 0;
      uVar8 = 0;
      do {
        lVar1 = *(long *)(param_2 + 8) + lVar7;
        FUN_ffff0000907c659c(local_158 + 9,*(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x10))
        ;
        uVar8 = uVar8 + 1;
        lVar7 = lVar7 + 0x20;
      } while (uVar8 < *(ulong *)(param_2 + 0x10));
    }
    uVar3 = FUN_ffff0000907c65e0(local_158 + 9);
    thunk_FUN_ffff00009077e858(local_158 + 5,uVar3,0x20);
    iVar2 = (**(code **)(param_1 + 0x68))
                      (param_1,s_avb_managed_verity_mode_ffff0000907ecf30,0x20,local_158 + 5);
    if (iVar2 == 0) {
LAB_ffff0000907bf8cc:
      uVar6 = 2;
      goto LAB_ffff0000907bf8d0;
    }
    uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar4 = &DAT_ffff0000907e07e7;
    pcVar5 = s_Error_writing_to_avb_managed_ver_ffff0000907ebb54;
  }
  else {
    if ((iVar2 == 0) && (local_158[0] == 0)) goto LAB_ffff0000907bf644;
    if (iVar2 == 0) {
      if (local_158[0] != 0x20) {
        uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        FUN_ffff0000907c0874
                  (uVar3,&DAT_ffff0000907ebe23,&DAT_ffff0000907ed742,s___ERROR__ffff0000907e0739,
                   s_Unexpected_number_of_bytes_read_f_ffff0000907f227e,0);
        iVar2 = 2;
        uVar6 = 1;
        goto LAB_ffff0000907bf8d0;
      }
      local_158[0x21] = 0;
      local_158[0x22] = 0;
      local_158[0x1f] = 0;
      local_158[0x20] = 0;
      local_158[0x1d] = 0;
      local_158[0x1e] = 0;
      local_158[0x1b] = 0;
      local_158[0x1c] = 0;
      local_158[0x19] = 0;
      local_158[0x1a] = 0;
      local_158[0x17] = 0;
      local_158[0x18] = 0;
      local_158[0x15] = 0;
      local_158[0x16] = 0;
      local_158[0x13] = 0;
      local_158[0x14] = 0;
      local_158[0x11] = 0;
      local_158[0x12] = 0;
      local_158[0xf] = 0;
      local_158[0x10] = 0;
      local_158[0xd] = 0;
      local_158[0xe] = 0;
      local_158[0xb] = 0;
      local_158[0xc] = 0;
      local_158[9] = 0;
      local_158[10] = 0;
      FUN_ffff0000907c6564(local_158 + 9);
      if (*(long *)(param_2 + 0x10) != 0) {
        lVar7 = 0;
        uVar8 = 0;
        do {
          lVar1 = *(long *)(param_2 + 8) + lVar7;
          FUN_ffff0000907c659c
                    (local_158 + 9,*(undefined8 *)(lVar1 + 8),*(undefined8 *)(lVar1 + 0x10));
          uVar8 = uVar8 + 1;
          lVar7 = lVar7 + 0x20;
        } while (uVar8 < *(ulong *)(param_2 + 0x10));
      }
      uVar3 = FUN_ffff0000907c65e0(local_158 + 9);
      thunk_FUN_ffff00009077e858(local_158 + 5,uVar3,0x20);
      iVar2 = thunk_FUN_ffff00009077e820(local_158 + 5,local_158 + 1,0x20);
      if (iVar2 != 0) {
        uVar6 = 1;
        FUN_ffff0000907669ac(1);
        iVar2 = (**(code **)(param_1 + 0x68))
                          (param_1,s_avb_managed_verity_mode_ffff0000907ecf30,0,local_158 + 5);
        if (iVar2 != 0) {
          uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
          FUN_ffff0000907c0874
                    (uVar3,&DAT_ffff0000907ebe23,&DAT_ffff0000907ea368,s___ERROR__ffff0000907e0739,
                     s_Error_clearing_avb_managed_verit_ffff0000907dd691,0);
        }
        goto LAB_ffff0000907bf8d0;
      }
      iVar2 = 0;
      goto LAB_ffff0000907bf8cc;
    }
    uVar3 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    puVar4 = &DAT_ffff0000907e3e33;
    pcVar5 = s_Error_reading_from_avb_managed_v_ffff0000907ffff3;
  }
  FUN_ffff0000907c0874(uVar3,&DAT_ffff0000907ebe23,puVar4,s___ERROR__ffff0000907e0739,pcVar5,0);
  uVar6 = 1;
LAB_ffff0000907bf8d0:
  *param_3 = uVar6;
  return iVar2;
}


