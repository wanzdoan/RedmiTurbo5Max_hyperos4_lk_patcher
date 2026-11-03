
void FUN_ffff000090713a14(void)

{
  undefined1 uVar1;
  int iVar2;
  undefined4 uVar3;
  uint uVar4;
  undefined4 uVar5;
  undefined4 uVar6;
  undefined4 uVar7;
  undefined8 uVar8;
  long lVar9;
  long lVar10;
  char *pcVar11;
  ulong uVar12;
  undefined8 local_270;
  ulong uStack_268;
  undefined8 local_260;
  undefined8 local_258;
  undefined8 uStack_250;
  undefined8 local_248;
  undefined8 uStack_240;
  undefined8 local_238;
  undefined8 uStack_230;
  undefined8 local_228;
  undefined8 uStack_220;
  undefined8 local_218;
  undefined8 uStack_210;
  undefined8 local_208;
  undefined8 uStack_200;
  undefined8 local_1f8;
  undefined8 uStack_1f0;
  undefined8 local_1e8;
  undefined8 uStack_1e0;
  undefined8 local_1d8;
  undefined8 uStack_1d0;
  undefined2 local_1c8;
  undefined8 local_1c0;
  undefined8 local_1b8;
  undefined8 uStack_1b0;
  undefined8 local_1a8;
  undefined8 local_1a0;
  undefined8 local_198;
  undefined8 uStack_190;
  undefined8 local_188;
  undefined8 uStack_180;
  undefined8 local_178;
  undefined8 uStack_170;
  undefined4 local_168;
  undefined8 local_160;
  undefined8 local_158;
  undefined8 uStack_150;
  undefined8 local_148;
  undefined8 uStack_140;
  undefined8 local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  int local_34;
  
  uVar8 = FUN_ffff00009071f790();
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  uStack_e0 = 0;
  local_f8 = 0;
  uStack_f0 = 0;
  local_108 = 0;
  uStack_100 = 0;
  local_118 = 0;
  uStack_110 = 0;
  local_128 = 0;
  uStack_120 = 0;
  local_138 = 0;
  uStack_130 = 0;
  local_148 = 0;
  uStack_140 = 0;
  local_158 = 0;
  uStack_150 = 0;
  local_160 = 0;
  local_168 = 0;
  local_178 = 0;
  uStack_170 = 0;
  local_188 = 0;
  uStack_180 = 0;
  local_198 = 0;
  uStack_190 = 0;
  local_1a8 = 0;
  local_1a0 = 0;
  local_1b8 = 0;
  uStack_1b0 = 0;
  local_1c0 = 0;
  lVar9 = FUN_ffff00009077f250();
  if (lVar9 != 0) {
    iVar2 = FUN_ffff00009077f6fc(&local_160,199,s_androidboot_slot_suffix__s_ffff0000907ea748,uVar8)
    ;
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s__s__d__snprintf_error__ffff0000907dc15f,s_androidboot_cmdline_ffff0000907e261c,
                 0xeb);
    }
    else {
      FUN_ffff000090719078(&local_160);
    }
  }
  uVar8 = FUN_ffff00009070af2c();
  iVar2 = FUN_ffff00009077f6fc(&local_160,199,s_androidboot_serialno__s_ffff0000907f5d66,uVar8);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__snprintf_error__ffff0000907dc15f,s_androidboot_cmdline_ffff0000907e261c,0xf2
              );
  }
  else {
    FUN_ffff000090719078(&local_160);
  }
  uVar3 = FUN_ffff000090785264();
  iVar2 = FUN_ffff00009077f6fc
                    (&local_160,199,s_androidboot_dtb_idx_0_androidboo_ffff0000907d58e7,uVar3);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__snprintf_error__ffff0000907dc15f,s_androidboot_cmdline_ffff0000907e261c,0xf9
              );
  }
  else {
    FUN_ffff000090719078(&local_160);
  }
  FUN_ffff00009071c6a0(&local_1c0);
  FUN_ffff00009077ee50(&local_160,0,200);
  iVar2 = FUN_ffff00009077f6fc
                    (&local_160,200,s_androidboot_hwc__s_androidboot_h_ffff0000907fcc52,
                     (long)&local_1a0 + 4,&local_1c0,&uStack_1b0);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__mi_hwid__snprintf_error__ffff0000907d7546,
               s_androidboot_cmdline_ffff0000907e261c,0x102);
  }
  else {
    FUN_ffff000090719078(&local_160);
  }
  local_34 = 0;
  FUN_ffff00009077e11c
            (s__s__d__hw_level___s__ffff0000907cf1ef,s_androidboot_cmdline_ffff0000907e261c,0x10c,
             &local_1c0);
  iVar2 = FUN_ffff000090788a10(&local_34);
  if (iVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__d__device_lock_state___u_ffff0000907e8e94,
               s_androidboot_cmdline_ffff0000907e261c,0x10f,local_34);
    if (local_34 == 3) {
      iVar2 = FUN_ffff00009077efc4(&local_1c0,&DAT_ffff0000907e8eb2);
      if (iVar2 != 0) {
        FUN_ffff0000907670fc();
        goto LAB_ffff000090713c08;
      }
      goto LAB_ffff000090713c14;
    }
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__get_device_lock_state_fai_ffff0000907ddaae,
               s_androidboot_cmdline_ffff0000907e261c,0x117);
LAB_ffff000090713c08:
    if (local_34 == 3) {
LAB_ffff000090713c14:
      iVar2 = FUN_ffff00009077efc4(&local_1c0,&DAT_ffff0000907e8eb2);
      if (iVar2 != 0) {
        uVar3 = FUN_ffff000090767404();
        iVar2 = FUN_ffff00009077f6fc
                          (&local_160,199,s_androidboot_db_encrypt__u_ffff0000907feafc,uVar3);
        if (iVar2 < 0) {
          FUN_ffff00009077e11c
                    (s__s__d__snprintf_error__ffff0000907dc15f,
                     s_androidboot_cmdline_ffff0000907e261c,0x11d);
        }
        else {
          FUN_ffff000090719078(&local_160);
        }
      }
    }
  }
  FUN_ffff00009077ee50(&local_160,0,200);
  uVar1 = thunk_FUN_ffff00009074b22c(0x1e);
  iVar2 = FUN_ffff00009077f6fc(&local_160,200,s_androidboot_cpusegment_0x_x_ffff0000907f7c98,uVar1);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__cpu_segment__snprintf_err_ffff0000907e761a,
               s_androidboot_cmdline_ffff0000907e261c,0x129);
  }
  else {
    FUN_ffff000090719078(&local_160);
  }
  FUN_ffff00009077ee50(&local_160,0,200);
  uVar4 = thunk_FUN_ffff00009074b22c(0x1b);
  iVar2 = FUN_ffff00009077f6fc
                    (&local_160,200,&DAT_ffff0000907e5c82,s_androidboot_secureboot__ffff0000907e434b
                     ,((uVar4 ^ 0xffffffff) & 0xe) == 0);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__snprintf_secureboot_error_ffff0000907f5d7e,
               s_androidboot_cmdline_ffff0000907e261c,0x134);
  }
  else {
    FUN_ffff000090719078(&local_160);
  }
  FUN_ffff00009077ee50(&local_160,0,200);
  iVar2 = FUN_ffff00009078395c();
  uVar3 = 0xb;
  if (iVar2 != 0xfc11) {
    uVar3 = 0;
  }
  uVar5 = 0x20;
  if (iVar2 != 0x3121) {
    uVar5 = uVar3;
  }
  iVar2 = FUN_ffff00009077f6fc
                    (&local_160,200,&DAT_ffff0000907f0e78,s_androidboot_dp_0x_ffff0000907feb16,uVar5
                    );
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__snprintf_dp_error__ffff0000907ef48e,s_androidboot_cmdline_ffff0000907e261c,
               0x141);
  }
  else {
    FUN_ffff000090719078(&local_160);
  }
  FUN_ffff00009077ee50(&local_160,0,200);
  uVar3 = thunk_FUN_ffff00009074b22c(0xc);
  uVar5 = thunk_FUN_ffff00009074b22c(0xd);
  uVar6 = thunk_FUN_ffff00009074b22c(0xe);
  uVar7 = thunk_FUN_ffff00009074b22c(0xf);
  iVar2 = FUN_ffff00009077f6fc
                    (&local_160,200,s__s_08x_08x_08x_08x_ffff0000907dbf7f,
                     s_androidboot_chipid_0x_ffff0000907f9486,uVar3,uVar5,uVar6,uVar7);
  if (iVar2 < 0) {
    FUN_ffff00009077e11c
              (s__s__d__snprintf_chipid_error__ffff0000907e0c1f,
               s_androidboot_cmdline_ffff0000907e261c,0x14d);
  }
  else {
    FUN_ffff000090719078(&local_160);
  }
  FUN_ffff00009077ee50(&local_160,0,200);
  local_1c8 = 0;
  local_1d8 = 0;
  uStack_1d0 = 0;
  local_1e8 = 0;
  uStack_1e0 = 0;
  local_1f8 = 0;
  uStack_1f0 = 0;
  local_208 = 0;
  uStack_200 = 0;
  local_218 = 0;
  uStack_210 = 0;
  local_228 = 0;
  uStack_220 = 0;
  local_238 = 0;
  uStack_230 = 0;
  local_248 = 0;
  uStack_240 = 0;
  local_258 = 0;
  uStack_250 = 0;
  local_260 = 0;
  iVar2 = FUN_ffff00009079482c(&local_260);
  if (iVar2 == 0) {
    local_88 = 0;
    local_80 = 0;
    local_98 = 0;
    local_90 = 0;
    lVar9 = FUN_ffff00009076fe0c((long)&uStack_250 + 3,0x81,&local_98);
    if (lVar9 == 0) {
      FUN_ffff00009077e11c
                (s__s__d__ERROR__CAN_NOT_SHA256_UFS_ffff0000907e4363,
                 s_androidboot_cmdline_ffff0000907e261c,0x15b);
      pcVar11 = s_hash_ufsid__failed____ffff0000907ebfd3;
      goto LAB_ffff000090713ed0;
    }
    FUN_ffff00009077f6fc
              (&local_160,200,s__s_08x_08x_08x_08x_ffff0000907dbf7f,
               s_androidboot_cpuid_0x_ffff0000907f5da1,uVar3,uVar5,uVar6,uVar7);
    FUN_ffff00009077f6fc((long)&uStack_130 + 4,0x94,&DAT_ffff0000907f5cad,local_98 & 0xff);
    FUN_ffff00009077f6fc((long)&uStack_130 + 6,0x92,&DAT_ffff0000907f5cad,local_98._1_1_);
    FUN_ffff00009077f6fc(&local_128,0x90,&DAT_ffff0000907f5cad,local_98._2_1_);
    FUN_ffff00009077f6fc((long)&local_128 + 2,0x8e,&DAT_ffff0000907f5cad,local_98._3_1_);
    FUN_ffff00009077f6fc((long)&local_128 + 4,0x8c,&DAT_ffff0000907f5cad,local_98._4_1_);
    FUN_ffff00009077f6fc((long)&local_128 + 6,0x8a,&DAT_ffff0000907f5cad,local_98._5_1_);
    FUN_ffff00009077f6fc(&uStack_120,0x88,&DAT_ffff0000907f5cad,local_98._6_1_);
    FUN_ffff00009077f6fc((long)&uStack_120 + 2,0x86,&DAT_ffff0000907f5cad,local_98._7_1_);
    FUN_ffff00009077f6fc((long)&uStack_120 + 4,0x84,&DAT_ffff0000907f5cad,local_90 & 0xff);
    FUN_ffff00009077f6fc((long)&uStack_120 + 6,0x82,&DAT_ffff0000907f5cad,local_90._1_1_);
    FUN_ffff00009077f6fc(&local_118,0x80,&DAT_ffff0000907f5cad,local_90._2_1_);
    iVar2 = FUN_ffff00009077f6fc((long)&local_118 + 2,0x7e,&DAT_ffff0000907f5cad,local_90._3_1_);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s__s__d__snprintf_cpuid_error__ffff0000907f41ec,
                 s_androidboot_cmdline_ffff0000907e261c,0x161);
    }
    else {
      FUN_ffff000090719078(&local_160);
    }
  }
  else {
    pcVar11 = s_ufs_id_get_failed____ffff0000907feb28;
LAB_ffff000090713ed0:
    FUN_ffff00009077e11c(pcVar11);
  }
  local_270 = 0;
  uStack_268 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  local_88 = 0;
  local_98 = 0;
  uVar8 = FUN_ffff00009071f790();
  iVar2 = FUN_ffff00009077f6fc(&local_98,0x47,s_countrycode_s_ffff0000907ebfea,uVar8);
  if (iVar2 < 1) {
    FUN_ffff00009077e11c(s_Fail_to_revise_countrycode_parti_ffff0000907cf224);
  }
  else {
    lVar9 = FUN_ffff00009077c028(&local_98);
    if (lVar9 == 0) {
      FUN_ffff00009077e11c(s_countrycode____s__is_not_exist__ffff0000907ddad4,&local_98);
    }
    else {
      lVar10 = FUN_ffff00009077c0bc(lVar9,&local_270,0,0x10);
      if (lVar10 == 0x10) {
        lVar10 = FUN_ffff00009077f250(&local_270);
        if (lVar10 != 0) {
          FUN_ffff00009077e11c(s_Get_countrycode_data_from_countr_ffff0000907e26b7,&local_270);
          FUN_ffff00009077bfac(lVar9);
          uStack_268 = uStack_268 & 0xffffffffffffff;
          FUN_ffff00009077ee50(&local_160,0,200);
          FUN_ffff00009077f6fc
                    (&local_160,200,s_androidboot_ptcountrycode__s_ffff0000907f278d,&local_270);
          FUN_ffff000090719078(&local_160);
          goto LAB_ffff00009071416c;
        }
        pcVar11 = s_countrycode_partition_data_is_em_ffff0000907e2692;
      }
      else {
        pcVar11 = s_Failed_to_get_countrycode_partit_ffff000090802002;
      }
      FUN_ffff00009077e11c(pcVar11);
      FUN_ffff00009077bfac(lVar9);
    }
  }
LAB_ffff00009071416c:
  local_98 = local_98 & 0xffffffff00000000;
  FUN_ffff00009077ee50(&local_160,0,200);
  FUN_ffff00009074a780(6,&local_98);
  pcVar11 = s_androidboot_sbc_epdev_0_ffff0000907e4388;
  if ((int)local_98 < 0) {
    pcVar11 = s_androidboot_sbc_epdev_1_ffff0000907f420a;
  }
  FUN_ffff00009077f6fc(&local_160,200,pcVar11);
  FUN_ffff000090719078(&local_160);
  FUN_ffff00009077ee50(&local_160,0,200);
  iVar2 = FUN_ffff00009072b0f8();
  pcVar11 = s_androidboot_uart_console_true_ffff0000907d0baf;
  if (iVar2 != 0x454e454e) {
    pcVar11 = s_androidboot_uart_console_false_ffff0000907cf205;
  }
  FUN_ffff00009077f6fc(&local_160,200,pcVar11);
  FUN_ffff000090719078(&local_160);
  FUN_ffff00009077ee50(&local_160,0,200);
  uVar4 = FUN_ffff00009077d72c(&uStack_1b0);
  switch(uVar4) {
  case 1:
  case 2:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907f5db6);
    FUN_ffff000090719078(&local_160);
    FUN_ffff00009077f6fc
              (&local_160,200,s_hwid_project_name_rodin_hwid_hwi_ffff0000907e5c87,
               local_1a0 & 0xffffffff,uVar4);
    FUN_ffff000090719078(&local_160);
    goto LAB_ffff000090714470;
  case 3:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907dc177);
    FUN_ffff000090719078(&local_160);
    uVar12 = local_1a0 & 0xffffffff;
    pcVar11 = s_hwid_project_name_dali_hwid_hwid_ffff0000907e2630;
    uVar8 = 3;
    break;
  case 4:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907d26c6);
    FUN_ffff000090719078(&local_160);
    pcVar11 = s_hwid_project_name_klimt_hwid_hwi_ffff0000907d26ed;
    uVar8 = 4;
    goto LAB_ffff00009071433c;
  case 5:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907d5915);
    FUN_ffff000090719078(&local_160);
    pcVar11 = s_hwid_project_name_goya_hwid_hwid_ffff0000907ea763;
    uVar8 = 5;
LAB_ffff00009071433c:
    FUN_ffff00009077f6fc(&local_160,200,pcVar11,(undefined4)local_1a0,uVar8);
    FUN_ffff000090719078(&local_160);
    goto LAB_ffff00009071447c;
  case 6:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907e266a);
    FUN_ffff000090719078(&local_160);
    uVar12 = local_1a0 & 0xffffffff;
    pcVar11 = s_hwid_project_name_turner_hwid_hw_ffff0000907fcc95;
    uVar8 = 6;
    break;
  case 7:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907edc07);
    FUN_ffff000090719078(&local_160);
    uVar12 = local_1a0 & 0xffffffff;
    pcVar11 = s_hwid_project_name_klee_hwid_hwid_ffff0000907f7cb4;
    uVar8 = 7;
    break;
  case 8:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907dc19d);
    FUN_ffff000090719078(&local_160);
    uVar12 = local_1a0 & 0xffffffff;
    pcVar11 = s_hwid_project_name_dash_hwid_hwid_ffff0000907e43a0;
    uVar8 = 8;
    break;
  case 9:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907ea79d);
    FUN_ffff000090719078(&local_160);
    uVar12 = local_1a0 & 0xffffffff;
    pcVar11 = s_hwid_project_name_chagall_hwid_h_ffff0000907f949c;
    uVar8 = 9;
    break;
  default:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907d593b);
    FUN_ffff00009077e11c
              (s__s__d__get_project_id__d__from_m_ffff0000907df4e2,
               s_androidboot_cmdline_ffff0000907e261c,0x1f5,uVar4);
LAB_ffff000090714470:
    if ((uVar4 & 0xfffffffe) != 4) {
      return;
    }
LAB_ffff00009071447c:
    FUN_ffff00009077ee50(&local_160,0,200);
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_theme_custom_ffff0000907dc1c3);
    goto LAB_ffff0000907144e0;
  case 0xb:
    FUN_ffff00009077f6fc(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907ea7c6);
    FUN_ffff000090719078(&local_160);
    uVar12 = local_1a0 & 0xffffffff;
    pcVar11 = s_hwid_project_name_sanjose_hwid_h_ffff0000907da687;
    uVar8 = 0xb;
  }
  FUN_ffff00009077f6fc(&local_160,200,pcVar11,uVar12,uVar8);
LAB_ffff0000907144e0:
  FUN_ffff000090719078(&local_160);
  return;
}


