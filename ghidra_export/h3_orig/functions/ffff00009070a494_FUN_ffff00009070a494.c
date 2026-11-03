// ffff00009070a494  size=1912  FUN_ffff00009070a494

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070a494(void)

{
  undefined4 *puVar1;
  undefined *puVar2;
  int iVar3;
  uint uVar4;
  undefined8 uVar5;
  ulong uVar6;
  undefined8 uVar7;
  char *pcVar8;
  long lVar9;
  int *piVar10;
  undefined8 *puVar11;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined4 local_48;
  undefined4 local_28;
  int local_24;
  
  FUN_ffff00009077de54
            (s__s__d__Wait_for_enter_fastboot_e_ffff000090801cd8,
             s_wait_for_enter_fastboot_ffff0000907d8b8f,0x7e);
  FUN_ffff000090717454(&LAB_ffff000090816ca0,0xffffffff);
  FUN_ffff000090717584(&LAB_ffff000090816ca0);
  FUN_ffff00009077de54
            (s__s__d__Ready_to_enter_fastboot___ffff0000907e72d0,
             s_wait_for_enter_fastboot_ffff0000907d8b8f,0x81);
  FUN_ffff000090718584(0xffff00009083d240);
  FUN_ffff00009071872c(0xffff00009083d240,5000,PTR_FUN_ffff000090839820,0);
  FUN_ffff00009077c844();
  FUN_ffff000090709964(s_getvar__ffff0000907e23e8,PTR_FUN_ffff0000908398b8,1,0);
  FUN_ffff000090709964(s_reboot_ffff0000907d8ba7,PTR_FUN_ffff0000908398c0,1,0);
  FUN_ffff000090709964(s_reboot_bootloader_ffff0000907e23f0,PTR_FUN_ffff0000908398c8,1,0);
  FUN_ffff000090709964(s_reboot_fastboot_ffff0000907fe817,PTR_FUN_ffff0000908398d0,1,0);
  FUN_ffff000090709964(s_reboot_recovery_ffff0000907f9168,PTR_FUN_ffff0000908398d8,1,0);
  FUN_ffff000090709964(s_continue_ffff0000907f0b79,PTR_FUN_ffff0000908398e0,0,0);
  FUN_ffff000090709964(s_set_active__ffff0000907fc982,PTR_FUN_ffff0000908398e8,1,0);
  FUN_ffff000090709964(s_oem_poweroff_ffff0000907e8b5d,PTR_FUN_ffff0000908398f0,1,0);
  FUN_ffff000090709a38(&DAT_ffff0000907f0b82,&DAT_ffff0000907d8bae);
  FUN_ffff00009070cbfc();
  puVar2 = PTR_PTR_ffff0000908398f8;
  for (puVar1 = (undefined4 *)PTR_LAB_ffff000090839900; puVar1 != (undefined4 *)puVar2;
      puVar1 = puVar1 + 6) {
    if (*(long *)(puVar1 + 4) != 0) {
      FUN_ffff000090709964(*(undefined8 *)(puVar1 + 2),*(long *)(puVar1 + 4),*puVar1,puVar1[1]);
    }
  }
  iVar3 = FUN_ffff00009072f42c(0,99);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff00009072eb50(0xffff00009083d1f0,0x40);
    if ((iVar3 < 1) &&
       (iVar3 = FUN_ffff00009077f434(0xffff00009083d1f0,0x40,&DAT_ffff0000907dd800), iVar3 < 0)) {
      FUN_ffff00009077de54
                (s_snprintf_error_in__s___d_ffff0000907da452,
                 s_app_fastboot_fastboot_entry_c_ffff0000907e0981,0x71);
    }
  }
  else {
    FUN_ffff00009077de54(s_Load_modem_for_md_version_failed_ffff0000907dd7d5,iVar3);
  }
  local_48 = 0;
  local_58 = 0;
  uStack_50 = 0;
  FUN_ffff00009077f38c(&local_58,&DAT_ffff0000907fe827,0x8000000);
  FUN_ffff000090709a38(s_version_ffff0000907f79b4,&DAT_ffff0000907d3ddb);
  uVar5 = FUN_ffff00009070bd00();
  FUN_ffff000090709a38(s_version_bootloader_ffff0000907df296,uVar5);
  FUN_ffff000090709a38(s_max_download_size_ffff0000907f79bc,&local_58);
  FUN_ffff000090709a38(s_product_ffff0000907d09fd,&DAT_ffff0000907d72a3);
  uVar5 = FUN_ffff00009070aeb4();
  FUN_ffff000090709a38(s_serialno_ffff0000907d24bb,uVar5);
  local_24 = 0;
  iVar3 = thunk_FUN_ffff0000907b5f54(&local_24);
  if (iVar3 == 0) {
    if (local_24 == 0) {
      uVar5 = FUN_ffff00009070c108();
      FUN_ffff000090709a38(s_cpuid_ffff0000907d3ddf,uVar5);
    }
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_query_lock_stat_ffff0000907e09ae,
               s_publish_attributes_ffff0000907fe82c,0xa5);
  }
  FUN_ffff000090709a38(s_is_userspace_ffff0000907e09dd,&DAT_ffff0000907f0b86);
  FUN_ffff000090709a38(s_protocol_version_ffff0000907df2a9,&DAT_ffff0000907d24c4);
  FUN_ffff000090709a38(&DAT_ffff0000907d3de5,&DAT_ffff0000907df2ba);
  local_28 = 0;
  iVar3 = FUN_ffff00009074a4e8(0,&local_28);
  if (iVar3 == 0) {
    if ((byte)local_28 < 4) {
                    /* WARNING: Could not recover jumptable at 0xffff00009070a834. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*(code *)((ulong)(byte)(&LAB_ffff000090803d00)[(byte)local_28] * 4 + -0xffff6f8f57c8))();
      return;
    }
    FUN_ffff00009077de54
              (s__s__d__Unknown_sec_hwc_ffff0000907ebd53,s_publish_attributes_ffff0000907fe82c,0xc5)
    ;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_read_efuse_in_f_ffff0000907ea521,
               s_publish_attributes_ffff0000907fe82c,200);
  }
  FUN_ffff000090709a38(s_version_baseband_ffff0000907e8b72,0xffff00009083d1f0);
  FUN_ffff00009070bd60();
  FUN_ffff00009070c334();
  uVar6 = FUN_ffff00009071f9e8();
  if ((uVar6 & 1) == 0) {
    pcVar8 = s_slot_count_ffff0000907e8b90;
    puVar11 = (undefined8 *)&DAT_ffff0000907d24c9;
  }
  else {
    uVar5 = FUN_ffff00009071f608();
    uVar7 = FUN_ffff00009077ef88(&DAT_ffff0000907d3dfd);
    iVar3 = FUN_ffff00009077f080(uVar5,&DAT_ffff0000907d3dfd,uVar7);
    puVar2 = &DAT_ffff0000907f0b89;
    if (iVar3 != 0) {
      puVar2 = &DAT_ffff0000907d3e00;
    }
    FUN_ffff000090709a38(s_current_slot_ffff0000907e8b83,puVar2);
    iVar3 = FUN_ffff00009077f38c(&local_58,&DAT_ffff000090801d01,2);
    if ((iVar3 < 0) && (piVar10 = (int *)FUN_ffff00009077d8f0(), *piVar10 != 0)) {
      FUN_ffff00009077de54
                (s__s__d__Get_BOOTCTRL_NUM_SLOTS_fa_ffff0000907f3d7c,
                 s_publish_attributes_ffff0000907fe82c,0xe4);
    }
    else {
      FUN_ffff000090709a38(s_slot_count_ffff0000907e8b90,&local_58);
    }
    iVar3 = FUN_ffff00009071f89c(0,0);
    if (-1 < iVar3) {
      puVar2 = &DAT_ffff0000907f0b86;
      if (iVar3 != 0) {
        puVar2 = &DAT_ffff0000907df2ba;
      }
      FUN_ffff000090709a38(s_slot_unbootable_a_ffff0000907e09ea,puVar2);
    }
    iVar3 = FUN_ffff00009071f89c(1,0);
    if (-1 < iVar3) {
      puVar2 = &DAT_ffff0000907f0b86;
      if (iVar3 != 0) {
        puVar2 = &DAT_ffff0000907df2ba;
      }
      FUN_ffff000090709a38(s_slot_unbootable_b_ffff0000907f3da0,puVar2);
    }
    iVar3 = FUN_ffff00009071f89c(0,1);
    if (-1 < iVar3) {
      puVar2 = &DAT_ffff0000907f0b86;
      if (iVar3 != 0) {
        puVar2 = &DAT_ffff0000907df2ba;
      }
      FUN_ffff000090709a38(s_slot_successful_a_ffff0000907dd804,puVar2);
    }
    iVar3 = FUN_ffff00009071f89c(1,1);
    if (-1 < iVar3) {
      puVar2 = &DAT_ffff0000907f0b86;
      if (iVar3 != 0) {
        puVar2 = &DAT_ffff0000907df2ba;
      }
      FUN_ffff000090709a38(s_slot_successful_b_ffff0000907dd816,puVar2);
    }
    iVar3 = FUN_ffff00009071f89c(0,2);
    if (-1 < iVar3) {
      iVar3 = FUN_ffff00009077f38c(&local_58,&DAT_ffff000090801d01,iVar3);
      if ((iVar3 < 0) && (piVar10 = (int *)FUN_ffff00009077d8f0(), *piVar10 != 0)) {
        FUN_ffff00009077de54
                  (s__s__d__Get_slot_retry_count_a_fa_ffff0000907da46c,
                   s_publish_attributes_ffff0000907fe82c,0xfc);
      }
      else {
        FUN_ffff000090709a38(s_slot_retry_count_a_ffff0000907f9178,&local_58);
      }
    }
    iVar3 = FUN_ffff00009071f89c(1,2);
    if (iVar3 < 0) goto LAB_ffff00009070aaf4;
    iVar3 = FUN_ffff00009077f38c(&local_58,&DAT_ffff000090801d01,iVar3);
    if ((iVar3 < 0) && (piVar10 = (int *)FUN_ffff00009077d8f0(), *piVar10 != 0)) {
      FUN_ffff00009077de54
                (s__s__d__Get_slot_retry_count_b_fa_ffff0000907ebd6b,
                 s_publish_attributes_ffff0000907fe82c,0x105);
      goto LAB_ffff00009070aaf4;
    }
    pcVar8 = s_slot_retry_count_b_ffff0000907f918b;
    puVar11 = &local_58;
  }
  FUN_ffff000090709a38(pcVar8,puVar11);
LAB_ffff00009070aaf4:
  uVar4 = FUN_ffff0000907204d8();
  if (uVar4 < 2) {
    puVar2 = &DAT_ffff0000907d24c9;
    if (uVar4 != 0) {
      puVar2 = &DAT_ffff0000907d8bae;
    }
    FUN_ffff000090709a38(s_off_mode_charge_ffff0000907fc98e,puVar2);
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__off_mode_charge_status_is_ffff0000907f919e,
               s_publish_attributes_ffff0000907fe82c,0x114);
  }
  lVar9 = FUN_ffff000090719798
                    (0x8000000,0x1000,0xffffffff,0,0,s_system_fastboot_buffer_ffff0000907e40c5);
  if (lVar9 != 0) {
    iVar3 = FUN_ffff00009077b79c
                      (DAT_ffff000090839908,s_fastboot_buffer_ffff0000907d24cb,0x8000000,
                       &DAT_ffff00009083d238,0xc,lVar9,0,0);
    if (iVar3 == 0) {
      FUN_ffff00009077eb88(_DAT_ffff00009083d238,0,0x8000000);
      if (_DAT_ffff00009083d238 == 0) {
        FUN_ffff00009077de54(s_Allocate_0x_x_bytes_failed__ffff0000907fc965,0x8000000);
      }
      else {
        FUN_ffff00009077de54
                  (s__s__d__fastboot_init_buf_0x_lx_s_ffff0000907f798a,
                   s_fastboot_entry_ffff0000907e099f,0x16b,_DAT_ffff00009083d238,0x8000000);
        FUN_ffff000090709dd0(_DAT_ffff00009083d238,0x8000000);
        uVar5 = FUN_ffff00009077b06c(_DAT_ffff00009083d238);
        FUN_ffff00009077de54(s_mblock_free_0x_lx_ffff0000907cf100,uVar5);
        FUN_ffff00009071a6ac(uVar5);
        FUN_ffff00009077bb68(DAT_ffff000090839908,_DAT_ffff00009083d238);
      }
      FUN_ffff000090709924();
      FUN_ffff0000907099dc();
      return;
    }
    FUN_ffff00009077de54(s_map_error_for_fastboot_buffer_ffff0000907e09fc);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_app_fastboot_fastboot_entry_c_ffff0000907e0981,0x148,&DAT_ffff0000907d24c9);
  }
  FUN_ffff00009077de54(s_fastboot_buffer_alloc_fails__ffff0000907d8bb3);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04(s_app_fastboot_fastboot_entry_c_ffff0000907e0981,0x141,&DAT_ffff0000907d24c9)
  ;
}


