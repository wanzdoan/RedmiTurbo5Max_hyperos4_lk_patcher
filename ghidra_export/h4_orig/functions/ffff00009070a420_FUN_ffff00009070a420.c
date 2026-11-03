// ffff00009070a420  size=2148  FUN_ffff00009070a420

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070a420(void)

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
  
  FUN_ffff00009077e11c
            (s__s__d__Wait_for_enter_fastboot_e_ffff000090801eb4,
             s_wait_for_enter_fastboot_ffff0000907d8c10,0x82);
  FUN_ffff000090717598(&LAB_ffff000090816cb0,0xffffffff);
  FUN_ffff0000907176c8(&LAB_ffff000090816cb0);
  FUN_ffff00009077e11c
            (s__s__d__Ready_to_enter_fastboot___ffff0000907e7414,
             s_wait_for_enter_fastboot_ffff0000907d8c10,0x85);
  FUN_ffff0000907186c8(0xffff00009083d1e0);
  FUN_ffff000090718870(0xffff00009083d1e0,5000,PTR_FUN_ffff000090839840,0);
  FUN_ffff00009077cb0c();
  FUN_ffff0000907098f0(s_getvar__ffff0000907e24b9,PTR_FUN_ffff0000908398d8,1,0);
  FUN_ffff0000907098f0(s_reboot_ffff0000907d8c28,PTR_FUN_ffff0000908398e0,1,0);
  FUN_ffff0000907098f0(s_reboot_bootloader_ffff0000907e24c1,PTR_FUN_ffff0000908398e8,1,0);
  FUN_ffff0000907098f0(s_reboot_fastboot_ffff0000907fe9bf,PTR_FUN_ffff0000908398f0,1,0);
  FUN_ffff0000907098f0(s_reboot_recovery_ffff0000907f9304,PTR_FUN_ffff0000908398f8,1,0);
  FUN_ffff0000907098f0(s_continue_ffff0000907f0cea,PTR_FUN_ffff000090839900,0,0);
  FUN_ffff0000907098f0(s_set_active__ffff0000907fcb1e,PTR_FUN_ffff000090839908,1,0);
  FUN_ffff0000907098f0(s_oem_poweroff_ffff0000907e8ca1,PTR_FUN_ffff000090839910,1,0);
  FUN_ffff0000907099c4(&DAT_ffff0000907f0cf3,&DAT_ffff0000907d8c2f);
  FUN_ffff00009070cc74();
  puVar2 = PTR_PTR_ffff000090839918;
  for (puVar1 = (undefined4 *)PTR_LAB_ffff000090839920; puVar1 != (undefined4 *)puVar2;
      puVar1 = puVar1 + 6) {
    if (*(long *)(puVar1 + 4) != 0) {
      FUN_ffff0000907098f0(*(undefined8 *)(puVar1 + 2),*(long *)(puVar1 + 4),*puVar1,puVar1[1]);
    }
  }
  iVar3 = FUN_ffff00009072f6c4(0,99);
  if (iVar3 == 0) {
    iVar3 = FUN_ffff00009072ede8(0xffff00009083d190,0x40);
    if ((iVar3 < 1) &&
       (iVar3 = FUN_ffff00009077f6fc(0xffff00009083d190,0x40,&DAT_ffff0000907dd8a3), iVar3 < 0)) {
      FUN_ffff00009077e11c
                (s_snprintf_error_in__s___d_ffff0000907da4e9,
                 s_app_fastboot_fastboot_entry_c_ffff0000907e0a24,0x75);
    }
  }
  else {
    FUN_ffff00009077e11c(s_Load_modem_for_md_version_failed_ffff0000907dd878,iVar3);
  }
  local_48 = 0;
  local_58 = 0;
  uStack_50 = 0;
  FUN_ffff00009077f654(&local_58,&DAT_ffff0000907fe9cf,0x8000000);
  FUN_ffff0000907099c4(s_version_ffff0000907f7b3e,&DAT_ffff0000907d3e18);
  uVar5 = FUN_ffff00009070bd78();
  FUN_ffff0000907099c4(s_version_bootloader_ffff0000907df339,uVar5);
  FUN_ffff0000907099c4(s_max_download_size_ffff0000907f7b46,&local_58);
  FUN_ffff0000907099c4(s_product_ffff0000907d0a14,&DAT_ffff0000907d7339);
  uVar5 = FUN_ffff00009070af2c();
  FUN_ffff0000907099c4(s_serialno_ffff0000907d24f8,uVar5);
  local_24 = 0;
  iVar3 = thunk_FUN_ffff0000907b65c4(&local_24);
  if (iVar3 == 0) {
    if (local_24 == 0) {
      uVar5 = FUN_ffff00009070c180();
      FUN_ffff0000907099c4(s_cpuid_ffff0000907d3e1c,uVar5);
    }
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__Failed_to_query_lock_stat_ffff0000907e0a51,
               s_publish_attributes_ffff0000907fe9d4,0xa9);
  }
  FUN_ffff0000907099c4(s_is_userspace_ffff0000907e0a80,&DAT_ffff0000907f0cf7);
  FUN_ffff0000907099c4(s_protocol_version_ffff0000907df34c,&DAT_ffff0000907d2501);
  FUN_ffff0000907099c4(&DAT_ffff0000907d3e22,&DAT_ffff0000907df35d);
  local_28 = 0;
  iVar3 = FUN_ffff00009074a780(0,&local_28);
  if (iVar3 == 0) {
    if ((byte)local_28 < 4) {
                    /* WARNING: Could not recover jumptable at 0xffff00009070a7c0. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
      (*(code *)((ulong)(byte)(&LAB_ffff000090803f28)[(byte)local_28] * 4 + -0xffff6f8f583c))();
      return;
    }
    FUN_ffff00009077e11c
              (s__s__d__Unknown_sec_hwc_ffff0000907ebed6,s_publish_attributes_ffff0000907fe9d4,0xc9)
    ;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__Failed_to_read_efuse_in_f_ffff0000907ea66a,
               s_publish_attributes_ffff0000907fe9d4,0xcc);
  }
  FUN_ffff0000907099c4(s_version_baseband_ffff0000907e8cb6,0xffff00009083d190);
  FUN_ffff00009070bdd8();
  FUN_ffff00009070c3ac();
  uVar6 = FUN_ffff00009071fb70();
  if ((uVar6 & 1) == 0) {
    pcVar8 = s_slot_count_ffff0000907e8cd4;
    puVar11 = (undefined8 *)&DAT_ffff0000907d2506;
  }
  else {
    uVar5 = FUN_ffff00009071f790();
    uVar7 = FUN_ffff00009077f250(&DAT_ffff0000907d3e3a);
    iVar3 = FUN_ffff00009077f348(uVar5,&DAT_ffff0000907d3e3a,uVar7);
    puVar2 = &DAT_ffff0000907f0cfa;
    if (iVar3 != 0) {
      puVar2 = &DAT_ffff0000907d3e3d;
    }
    FUN_ffff0000907099c4(s_current_slot_ffff0000907e8cc7,puVar2);
    iVar3 = FUN_ffff00009077f654(&local_58,&DAT_ffff000090801edd,2);
    if ((iVar3 < 0) && (piVar10 = (int *)FUN_ffff00009077dbb8(), *piVar10 != 0)) {
      FUN_ffff00009077e11c
                (s__s__d__Get_BOOTCTRL_NUM_SLOTS_fa_ffff0000907f3f08,
                 s_publish_attributes_ffff0000907fe9d4,0xe8);
    }
    else {
      FUN_ffff0000907099c4(s_slot_count_ffff0000907e8cd4,&local_58);
    }
    iVar3 = FUN_ffff00009071fa24(0,0);
    if (-1 < iVar3) {
      puVar2 = &DAT_ffff0000907f0cf7;
      if (iVar3 != 0) {
        puVar2 = &DAT_ffff0000907df35d;
      }
      FUN_ffff0000907099c4(s_slot_unbootable_a_ffff0000907e0a8d,puVar2);
    }
    iVar3 = FUN_ffff00009071fa24(1,0);
    if (-1 < iVar3) {
      puVar2 = &DAT_ffff0000907f0cf7;
      if (iVar3 != 0) {
        puVar2 = &DAT_ffff0000907df35d;
      }
      FUN_ffff0000907099c4(s_slot_unbootable_b_ffff0000907f3f2c,puVar2);
    }
    iVar3 = FUN_ffff00009071fa24(0,1);
    if (-1 < iVar3) {
      puVar2 = &DAT_ffff0000907f0cf7;
      if (iVar3 != 0) {
        puVar2 = &DAT_ffff0000907df35d;
      }
      FUN_ffff0000907099c4(s_slot_successful_a_ffff0000907dd8a7,puVar2);
    }
    iVar3 = FUN_ffff00009071fa24(1,1);
    if (-1 < iVar3) {
      puVar2 = &DAT_ffff0000907f0cf7;
      if (iVar3 != 0) {
        puVar2 = &DAT_ffff0000907df35d;
      }
      FUN_ffff0000907099c4(s_slot_successful_b_ffff0000907dd8b9,puVar2);
    }
    iVar3 = FUN_ffff00009071fa24(0,2);
    if (-1 < iVar3) {
      iVar3 = FUN_ffff00009077f654(&local_58,&DAT_ffff000090801edd,iVar3);
      if ((iVar3 < 0) && (piVar10 = (int *)FUN_ffff00009077dbb8(), *piVar10 != 0)) {
        FUN_ffff00009077e11c
                  (s__s__d__Get_slot_retry_count_a_fa_ffff0000907da503,
                   s_publish_attributes_ffff0000907fe9d4,0x100);
      }
      else {
        FUN_ffff0000907099c4(s_slot_retry_count_a_ffff0000907f9314,&local_58);
      }
    }
    iVar3 = FUN_ffff00009071fa24(1,2);
    if (iVar3 < 0) goto LAB_ffff00009070aa80;
    iVar3 = FUN_ffff00009077f654(&local_58,&DAT_ffff000090801edd,iVar3);
    if ((iVar3 < 0) && (piVar10 = (int *)FUN_ffff00009077dbb8(), *piVar10 != 0)) {
      FUN_ffff00009077e11c
                (s__s__d__Get_slot_retry_count_b_fa_ffff0000907ebeee,
                 s_publish_attributes_ffff0000907fe9d4,0x109);
      goto LAB_ffff00009070aa80;
    }
    pcVar8 = s_slot_retry_count_b_ffff0000907f9327;
    puVar11 = &local_58;
  }
  FUN_ffff0000907099c4(pcVar8,puVar11);
LAB_ffff00009070aa80:
  uVar4 = FUN_ffff000090720660();
  if (uVar4 < 2) {
    puVar2 = &DAT_ffff0000907d2506;
    if (uVar4 != 0) {
      puVar2 = &DAT_ffff0000907d8c2f;
    }
    FUN_ffff0000907099c4(s_off_mode_charge_ffff0000907fcb2a,puVar2);
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__off_mode_charge_status_is_ffff0000907f933a,
               s_publish_attributes_ffff0000907fe9d4,0x118);
  }
  FUN_ffff00009071c6a0(0xffff00009083d210);
  FUN_ffff0000907099c4(&DAT_ffff0000907fcb3a,0xffff00009083d234);
  FUN_ffff0000907099c4(s_hwlevel_ffff0000907fcb3e,0xffff00009083d210);
  FUN_ffff0000907099c4(s_hwversion_ffff0000907d8c34,0xffff00009083d220);
  FUN_ffff00009077ee50(0xffff00009083d26c,0,0x20);
  iVar3 = FUN_ffff00009077f654(0xffff00009083d26c,&DAT_ffff000090801edd,_DAT_ffff00009083d264);
  if (iVar3 < 0) {
    FUN_ffff00009077e11c(s_project_adc_error_ffff0000907d576d);
  }
  else {
    FUN_ffff0000907099c4(s_project_adc_ffff0000907d8c3e,0xffff00009083d26c);
  }
  FUN_ffff00009077ee50(0xffff00009083d28c,0,0x20);
  iVar3 = FUN_ffff00009077f654(0xffff00009083d28c,&DAT_ffff000090801edd,_DAT_ffff00009083d268);
  if (iVar3 < 0) {
    FUN_ffff00009077e11c(s_build_adc_error_ffff0000907ea693);
  }
  else {
    FUN_ffff0000907099c4(s_build_adc_ffff000090801ee0,0xffff00009083d28c);
  }
  lVar9 = FUN_ffff0000907198dc
                    (0x8000000,0x1000,0xffffffff,0,0,s_system_fastboot_buffer_ffff0000907e4196);
  if (lVar9 != 0) {
    iVar3 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,s_fastboot_buffer_ffff0000907d2508,0x8000000,
                       &DAT_ffff00009083d1d8,0xc,lVar9,0,0);
    if (iVar3 == 0) {
      FUN_ffff00009077ee50(_DAT_ffff00009083d1d8,0,0x8000000);
      if (_DAT_ffff00009083d1d8 == 0) {
        FUN_ffff00009077e11c(s_Allocate_0x_x_bytes_failed__ffff0000907fcb01,0x8000000);
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__d__fastboot_init_buf_0x_lx_s_ffff0000907f7b14,
                   s_fastboot_entry_ffff0000907e0a42,0x188,_DAT_ffff00009083d1d8,0x8000000);
        FUN_ffff000090709d5c(_DAT_ffff00009083d1d8,0x8000000);
        uVar5 = FUN_ffff00009077b334(_DAT_ffff00009083d1d8);
        FUN_ffff00009077e11c(s_mblock_free_0x_lx_ffff0000907cf100,uVar5);
        FUN_ffff00009071a7f0(uVar5);
        FUN_ffff00009077be30(DAT_ffff000090839928,_DAT_ffff00009083d1d8);
      }
      FUN_ffff0000907098b0();
      FUN_ffff000090709968();
      return;
    }
    FUN_ffff00009077e11c(s_map_error_for_fastboot_buffer_ffff0000907e0a9f);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_app_fastboot_fastboot_entry_c_ffff0000907e0a24,0x165,&DAT_ffff0000907d2506);
  }
  FUN_ffff00009077e11c(s_fastboot_buffer_alloc_fails__ffff0000907d8c4a);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc(s_app_fastboot_fastboot_entry_c_ffff0000907e0a24,0x15e,&DAT_ffff0000907d2506)
  ;
}


