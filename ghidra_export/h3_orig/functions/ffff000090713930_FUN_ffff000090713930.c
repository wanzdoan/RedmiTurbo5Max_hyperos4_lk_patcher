// ffff000090713930  size=2708  FUN_ffff000090713930

void FUN_ffff000090713930(void)

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
  ulong uVar10;
  long lVar11;
  char *pcVar12;
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
  
  uVar8 = FUN_ffff00009071f608();
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
  lVar9 = FUN_ffff00009077ef88();
  if (lVar9 != 0) {
    iVar2 = FUN_ffff00009077f434(&local_160,199,s_androidboot_slot_suffix__s_ffff0000907ea5ee,uVar8)
    ;
    if (iVar2 < 0) {
      FUN_ffff00009077de54
                (s__s__d__snprintf_error__ffff0000907dc089,s_androidboot_cmdline_ffff0000907e254b,
                 0xea);
    }
    else {
      FUN_ffff000090718f34(&local_160);
    }
  }
  uVar8 = FUN_ffff00009070aeb4();
  iVar2 = FUN_ffff00009077f434(&local_160,199,s_androidboot_serialno__s_ffff0000907f5bef,uVar8);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d__snprintf_error__ffff0000907dc089,s_androidboot_cmdline_ffff0000907e254b,0xf1
              );
  }
  else {
    FUN_ffff000090718f34(&local_160);
  }
  uVar3 = FUN_ffff000090784f9c();
  iVar2 = FUN_ffff00009077f434
                    (&local_160,199,s_androidboot_dtb_idx_0_androidboo_ffff0000907d5897,uVar3);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d__snprintf_error__ffff0000907dc089,s_androidboot_cmdline_ffff0000907e254b,0xf8
              );
  }
  else {
    FUN_ffff000090718f34(&local_160);
  }
  FUN_ffff0000907034f8(&local_1c0);
  FUN_ffff00009077eb88(&local_160,0,200);
  iVar2 = FUN_ffff00009077f434
                    (&local_160,200,s_androidboot_hwc__s_androidboot_h_ffff0000907fcaaa,
                     (long)&local_1a0 + 4,&local_1c0,&uStack_1b0);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d__mi_hwid__snprintf_error__ffff0000907d74b0,
               s_androidboot_cmdline_ffff0000907e254b,0x101);
  }
  else {
    FUN_ffff000090718f34(&local_160);
  }
  local_34 = 0;
  FUN_ffff00009077de54
            (s__s__d__hw_level___s__ffff0000907cf1ef,s_androidboot_cmdline_ffff0000907e254b,0x10b,
             &local_1c0);
  iVar2 = FUN_ffff000090788750(&local_34);
  if (iVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__d__device_lock_state___u_ffff0000907e8d50,
               s_androidboot_cmdline_ffff0000907e254b,0x10e,local_34);
    if (local_34 == 3) {
      iVar2 = FUN_ffff00009077ecfc(&local_1c0,&DAT_ffff0000907e8d6e);
      if (iVar2 != 0) {
        uVar10 = FUN_ffff000090766e64();
        if ((uVar10 & 1) != 0) {
          FUN_ffff00009077de54
                    (s__s__d__set_selinux_to_permissive_ffff0000907f7ada,
                     s_androidboot_cmdline_ffff0000907e254b,0x111);
          FUN_ffff000090718f34(s_androidboot_selinux_permissive_ffff0000907f4060);
        }
        goto LAB_ffff000090713b24;
      }
      goto LAB_ffff000090713b30;
    }
  }
  else {
    FUN_ffff00009077de54
              (s__s__d__get_device_lock_state_fai_ffff0000907dda0b,
               s_androidboot_cmdline_ffff0000907e254b,0x116);
LAB_ffff000090713b24:
    if (local_34 == 3) {
LAB_ffff000090713b30:
      iVar2 = FUN_ffff00009077ecfc(&local_1c0,&DAT_ffff0000907e8d6e);
      if (iVar2 != 0) {
        uVar3 = FUN_ffff00009076716c();
        iVar2 = FUN_ffff00009077f434
                          (&local_160,199,s_androidboot_db_encrypt__u_ffff0000907fe954,uVar3);
        if (iVar2 < 0) {
          FUN_ffff00009077de54
                    (s__s__d__snprintf_error__ffff0000907dc089,
                     s_androidboot_cmdline_ffff0000907e254b,0x11c);
        }
        else {
          FUN_ffff000090718f34(&local_160);
        }
      }
    }
  }
  FUN_ffff00009077eb88(&local_160,0,200);
  uVar1 = thunk_FUN_ffff00009074af94(0x1e);
  iVar2 = FUN_ffff00009077f434(&local_160,200,s_androidboot_cpusegment_0x_x_ffff0000907f7afc,uVar1);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d__cpu_segment__snprintf_err_ffff0000907e74d6,
               s_androidboot_cmdline_ffff0000907e254b,0x128);
  }
  else {
    FUN_ffff000090718f34(&local_160);
  }
  FUN_ffff00009077eb88(&local_160,0,200);
  uVar4 = thunk_FUN_ffff00009074af94(0x1b);
  iVar2 = FUN_ffff00009077f434
                    (&local_160,200,&DAT_ffff0000907e5b6e,s_androidboot_secureboot__ffff0000907e4247
                     ,((uVar4 ^ 0xffffffff) & 0xe) == 0);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d__snprintf_secureboot_error_ffff0000907f5c07,
               s_androidboot_cmdline_ffff0000907e254b,0x133);
  }
  else {
    FUN_ffff000090718f34(&local_160);
  }
  FUN_ffff00009077eb88(&local_160,0,200);
  iVar2 = FUN_ffff000090783694();
  uVar3 = 0xb;
  if (iVar2 != 0xfc11) {
    uVar3 = 0;
  }
  uVar5 = 0x20;
  if (iVar2 != 0x3121) {
    uVar5 = uVar3;
  }
  iVar2 = FUN_ffff00009077f434
                    (&local_160,200,&DAT_ffff0000907f0d07,s_androidboot_dp_0x_ffff0000907fe96e,uVar5
                    );
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d__snprintf_dp_error__ffff0000907ef31d,s_androidboot_cmdline_ffff0000907e254b,
               0x140);
  }
  else {
    FUN_ffff000090718f34(&local_160);
  }
  FUN_ffff00009077eb88(&local_160,0,200);
  uVar3 = thunk_FUN_ffff00009074af94(0xc);
  uVar5 = thunk_FUN_ffff00009074af94(0xd);
  uVar6 = thunk_FUN_ffff00009074af94(0xe);
  uVar7 = thunk_FUN_ffff00009074af94(0xf);
  iVar2 = FUN_ffff00009077f434
                    (&local_160,200,s__s_08x_08x_08x_08x_ffff0000907dbe74,
                     s_androidboot_chipid_0x_ffff0000907f92ea,uVar3,uVar5,uVar6,uVar7);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d__snprintf_chipid_error__ffff0000907e0b7c,
               s_androidboot_cmdline_ffff0000907e254b,0x14c);
  }
  else {
    FUN_ffff000090718f34(&local_160);
  }
  FUN_ffff00009077eb88(&local_160,0,200);
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
  iVar2 = FUN_ffff00009079456c(&local_260);
  if (iVar2 == 0) {
    local_88 = 0;
    local_80 = 0;
    local_98 = 0;
    local_90 = 0;
    lVar9 = FUN_ffff00009076fb44((long)&uStack_250 + 3,0x81,&local_98);
    if (lVar9 == 0) {
      FUN_ffff00009077de54
                (s__s__d__ERROR__CAN_NOT_SHA256_UFS_ffff0000907e425f,
                 s_androidboot_cmdline_ffff0000907e254b,0x15a);
      pcVar12 = s_hash_ufsid__failed____ffff0000907ebe50;
      goto LAB_ffff000090713e14;
    }
    FUN_ffff00009077f434
              (&local_160,200,s__s_08x_08x_08x_08x_ffff0000907dbe74,
               s_androidboot_cpuid_0x_ffff0000907f5c2a,uVar3,uVar5,uVar6,uVar7);
    FUN_ffff00009077f434((long)&uStack_130 + 4,0x94,&DAT_ffff0000907f5b36,local_98 & 0xff);
    FUN_ffff00009077f434((long)&uStack_130 + 6,0x92,&DAT_ffff0000907f5b36,local_98._1_1_);
    FUN_ffff00009077f434(&local_128,0x90,&DAT_ffff0000907f5b36,local_98._2_1_);
    FUN_ffff00009077f434((long)&local_128 + 2,0x8e,&DAT_ffff0000907f5b36,local_98._3_1_);
    FUN_ffff00009077f434((long)&local_128 + 4,0x8c,&DAT_ffff0000907f5b36,local_98._4_1_);
    FUN_ffff00009077f434((long)&local_128 + 6,0x8a,&DAT_ffff0000907f5b36,local_98._5_1_);
    FUN_ffff00009077f434(&uStack_120,0x88,&DAT_ffff0000907f5b36,local_98._6_1_);
    FUN_ffff00009077f434((long)&uStack_120 + 2,0x86,&DAT_ffff0000907f5b36,local_98._7_1_);
    FUN_ffff00009077f434((long)&uStack_120 + 4,0x84,&DAT_ffff0000907f5b36,local_90 & 0xff);
    FUN_ffff00009077f434((long)&uStack_120 + 6,0x82,&DAT_ffff0000907f5b36,local_90._1_1_);
    FUN_ffff00009077f434(&local_118,0x80,&DAT_ffff0000907f5b36,local_90._2_1_);
    iVar2 = FUN_ffff00009077f434((long)&local_118 + 2,0x7e,&DAT_ffff0000907f5b36,local_90._3_1_);
    if (iVar2 < 0) {
      FUN_ffff00009077de54
                (s__s__d__snprintf_cpuid_error__ffff0000907f407f,
                 s_androidboot_cmdline_ffff0000907e254b,0x160);
    }
    else {
      FUN_ffff000090718f34(&local_160);
    }
  }
  else {
    pcVar12 = s_ufs_id_get_failed____ffff0000907fe980;
LAB_ffff000090713e14:
    FUN_ffff00009077de54(pcVar12);
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
  uVar8 = FUN_ffff00009071f608();
  iVar2 = FUN_ffff00009077f434(&local_98,0x47,s_countrycode_s_ffff0000907ebe67,uVar8);
  if (iVar2 < 1) {
    FUN_ffff00009077de54(s_Fail_to_revise_countrycode_parti_ffff0000907cf205);
  }
  else {
    lVar9 = FUN_ffff00009077bd60(&local_98);
    if (lVar9 == 0) {
      FUN_ffff00009077de54(s_countrycode____s__is_not_exist__ffff0000907dda31,&local_98);
    }
    else {
      lVar11 = FUN_ffff00009077bdf4(lVar9,&local_270,0,0x10);
      if (lVar11 == 0x10) {
        lVar11 = FUN_ffff00009077ef88(&local_270);
        if (lVar11 != 0) {
          FUN_ffff00009077de54(s_Get_countrycode_data_from_countr_ffff0000907e25e6,&local_270);
          FUN_ffff00009077bce4(lVar9);
          uStack_268 = uStack_268 & 0xffffffffffffff;
          FUN_ffff00009077eb88(&local_160,0,200);
          FUN_ffff00009077f434
                    (&local_160,200,s_androidboot_ptcountrycode__s_ffff0000907f2601,&local_270);
          FUN_ffff000090718f34(&local_160);
          goto LAB_ffff0000907140b0;
        }
        pcVar12 = s_countrycode_partition_data_is_em_ffff0000907e25c1;
      }
      else {
        pcVar12 = s_Failed_to_get_countrycode_partit_ffff000090801e1c;
      }
      FUN_ffff00009077de54(pcVar12);
      FUN_ffff00009077bce4(lVar9);
    }
  }
LAB_ffff0000907140b0:
  local_98 = local_98 & 0xffffffff00000000;
  FUN_ffff00009077eb88(&local_160,0,200);
  FUN_ffff00009074a4e8(6,&local_98);
  pcVar12 = s_androidboot_sbc_epdev_0_ffff0000907e4284;
  if ((int)local_98 < 0) {
    pcVar12 = s_androidboot_sbc_epdev_1_ffff0000907f409d;
  }
  FUN_ffff00009077f434(&local_160,200,pcVar12);
  FUN_ffff000090718f34(&local_160);
  FUN_ffff00009077eb88(&local_160,0,200);
  uVar4 = FUN_ffff00009077d464(&uStack_1b0);
  switch(uVar4) {
  case 1:
  case 2:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907f5c3f);
    FUN_ffff000090718f34(&local_160);
    FUN_ffff00009077f434
              (&local_160,200,s_hwid_project_name_rodin_hwid_hwi_ffff0000907e5b73,
               local_1a0 & 0xffffffff,uVar4);
    FUN_ffff000090718f34(&local_160);
    goto LAB_ffff0000907141b8;
  case 3:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907dc0a1);
    FUN_ffff000090718f34(&local_160);
    uVar10 = local_1a0 & 0xffffffff;
    pcVar12 = s_hwid_project_name_dali_hwid_hwid_ffff0000907e255f;
    uVar8 = 3;
    break;
  case 4:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907d2689);
    FUN_ffff000090718f34(&local_160);
    pcVar12 = s_hwid_project_name_klimt_hwid_hwi_ffff0000907d26b0;
    uVar8 = 4;
    goto LAB_ffff000090714278;
  case 5:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907d58c5);
    FUN_ffff000090718f34(&local_160);
    pcVar12 = s_hwid_project_name_goya_hwid_hwid_ffff0000907ea609;
    uVar8 = 5;
LAB_ffff000090714278:
    FUN_ffff00009077f434(&local_160,200,pcVar12,(undefined4)local_1a0,uVar8);
    FUN_ffff000090718f34(&local_160);
    goto LAB_ffff000090714284;
  case 6:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907e2599);
    FUN_ffff000090718f34(&local_160);
    uVar10 = local_1a0 & 0xffffffff;
    pcVar12 = s_hwid_project_name_turner_hwid_hw_ffff0000907fcaed;
    uVar8 = 6;
    break;
  case 7:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907eda96);
    FUN_ffff000090718f34(&local_160);
    uVar10 = local_1a0 & 0xffffffff;
    pcVar12 = s_hwid_project_name_klee_hwid_hwid_ffff0000907f7b18;
    uVar8 = 7;
    break;
  case 8:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907dc0c7);
    FUN_ffff000090718f34(&local_160);
    uVar10 = local_1a0 & 0xffffffff;
    pcVar12 = s_hwid_project_name_dash_hwid_hwid_ffff0000907e429c;
    uVar8 = 8;
    break;
  case 9:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907ea643);
    FUN_ffff000090718f34(&local_160);
    uVar10 = local_1a0 & 0xffffffff;
    pcVar12 = s_hwid_project_name_chagall_hwid_h_ffff0000907f9300;
    uVar8 = 9;
    break;
  default:
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_hardware_sku_ffff0000907d58eb);
    FUN_ffff00009077de54
              (s__s__d__get_project_id__d__from_m_ffff0000907df43f,
               s_androidboot_cmdline_ffff0000907e254b,0x1e6,uVar4);
LAB_ffff0000907141b8:
    if ((uVar4 & 0xfffffffe) != 4) {
      return;
    }
LAB_ffff000090714284:
    FUN_ffff00009077eb88(&local_160,0,200);
    FUN_ffff00009077f434(&local_160,200,s_androidboot_product_theme_custom_ffff0000907dc0ed);
    goto LAB_ffff00009071439c;
  }
  FUN_ffff00009077f434(&local_160,200,pcVar12,uVar10,uVar8);
LAB_ffff00009071439c:
  FUN_ffff000090718f34(&local_160);
  return;
}


