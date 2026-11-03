
int FUN_ffff000090791a54(long *param_1)

{
  bool bVar1;
  byte bVar2;
  uint uVar3;
  undefined8 uVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  int iVar8;
  int iVar9;
  int iVar10;
  char *pcVar11;
  uint uVar12;
  long lVar13;
  long lVar14;
  ulong uVar15;
  undefined4 local_a4;
  undefined8 local_a0;
  undefined8 local_98;
  int local_90;
  undefined4 uStack_8c;
  undefined4 local_88;
  undefined4 uStack_84;
  undefined4 local_80;
  undefined4 uStack_7c;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  uVar4 = DAT_ffff000090812514;
  iVar6 = 3;
LAB_ffff000090791ac4:
  local_98 = local_98 & 0xffffffff00000000;
  local_88 = 0;
  uStack_84 = 0;
  local_80 = 0;
  uStack_7c = 0xff;
  local_90 = (int)uVar4;
  uStack_8c = (undefined4)((ulong)uVar4 >> 0x20);
  FUN_ffff00009075fb10();
  if ((int)param_1[8] < 0) {
    *(uint *)(*param_1 + 0x24) = *(uint *)(*param_1 + 0x24) | 0x400;
    FUN_ffff00009078fe48(param_1,&local_90,1);
    (*(code *)param_1[0x47])(param_1,0xd0b50000,&local_98);
    local_98 = CONCAT44(local_98._4_4_,0x13c);
    (*(code *)param_1[0x49])(param_1,0xd0b50000,0x13c);
  }
  (*(code *)param_1[0x47])(param_1,0xd0a60000,&local_98);
  local_98 = local_98 & 0xffffffffffffffbf;
  (*(code *)param_1[0x49])(param_1,0xd0a60000);
  (*(code *)param_1[0x49])(param_1,0x15850000,0);
  local_98 = local_98 & 0xffffffff;
  (*(code *)param_1[0x47])(param_1,0xd0a60000,(long)&local_98 + 4);
  local_98 = local_98 & 0xfffffff2ffffffff;
  (*(code *)param_1[0x49])(param_1,0xd0a60000);
  (*(code *)param_1[0x47])(param_1,0xd0a10000,(long)&local_98 + 4);
  local_98 = local_98 | 0x2000000000;
  (*(code *)param_1[0x49])(param_1,0xd0a10000);
  lVar13 = *param_1;
  uStack_7c = 0;
  uStack_8c = 0;
  local_88 = 0;
  uStack_84 = 0;
  local_80 = 0;
  local_90 = 0x16;
  if ((*(uint *)(lVar13 + 0x30) >> 3 & 1) == 0) {
    pcVar11 = s_link__UIC_command_fail_ffff0000907d2036;
LAB_ffff000090791cb8:
    FUN_ffff00009077e11c(pcVar11);
    FUN_ffff000090797118(param_1,0,0xa0);
    FUN_ffff000090797118(param_1,0x100,0x74);
    local_90 = 0;
    (*(code *)param_1[0x47])(param_1,0xd0000000,&local_90);
    FUN_ffff00009077e11c(s__UFS__DME_LayerEnable_0x_x____0x_ffff0000907f0823,0xd000,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0200000,&local_90);
    FUN_ffff00009077e11c(s__UFS__DME_LinkStartup_0x_x____0x_ffff0000907e0561,0xd020,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0600000,&local_90);
    FUN_ffff00009077e11c(s__UFS__DME_ErrorPHYInd_0x_x____0x_ffff0000907d0475,0xd060,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0610000,&local_90);
    FUN_ffff00009077e11c(s__UFS__DME_ErrorPAInd_0x_x____0x__ffff0000907f0847,0xd061,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0830000,&local_90);
    FUN_ffff00009077e11c(s__UFS__PowerState_0x_x____0x_x_ffff0000907d8741,0xd083,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0950000,&local_90);
    FUN_ffff00009077e11c(s__UFS__DebugLinkStartup_0x_x____0_ffff0000907e56cd,0xd095,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0970000,&local_90);
    FUN_ffff00009077e11c(s__UFS__DebugStates_0x_x____0x_x_ffff0000907f7437,0xd097,local_90);
    (*(code *)param_1[0x47])(param_1,0x410000,&local_90);
    FUN_ffff00009077e11c(s__UFS__TX0_FSM_State_0x_x____0x_x_ffff0000907faae7,0x41,local_90);
    (*(code *)param_1[0x47])(param_1,0x410001,&local_90);
    FUN_ffff00009077e11c(s__UFS__TX1_FSM_State_0x_x____0x_x_ffff0000907ed4ee,0x41,local_90);
    (*(code *)param_1[0x47])(param_1,0xc10004,&local_90);
    FUN_ffff00009077e11c(s__UFS__RX0_FSM_State_0x_x____0x_x_ffff00009080320f,0xc1,local_90);
    (*(code *)param_1[0x47])(param_1,0xc10005,&local_90);
    FUN_ffff00009077e11c(s__UFS__RX1_FSM_State_0x_x____0x_x_ffff0000907eef81,0xc1,local_90);
    FUN_ffff00009075fb50(param_1);
    FUN_ffff00009075fe64();
    FUN_ffff00009077e11c(s_link_startup_fail__retrying_the___ffff0000907e212c,4 - iVar6);
    iVar5 = FUN_ffff0000907918fc(param_1);
    iVar7 = -1;
    if ((iVar5 != 0) || (bVar1 = iVar6 == 0, iVar6 = iVar6 + -1, bVar1)) goto LAB_ffff000090792240;
    goto LAB_ffff000090791ac4;
  }
  uVar15 = 0xffffffffffffff9c;
  param_1[10] = (long)&local_90;
  *(undefined4 *)(lVar13 + 0x94) = 0;
  *(undefined4 *)(lVar13 + 0x98) = 0;
  *(undefined4 *)(lVar13 + 0x9c) = 0;
  *(undefined4 *)(lVar13 + 0x90) = 0x16;
  do {
    lVar13 = *param_1;
    if ((*(uint *)(lVar13 + 0x20) >> 10 & 1) != 0) goto LAB_ffff000090791c78;
    FUN_ffff00009077cea0(100);
    uVar15 = uVar15 + 100;
  } while (uVar15 < 0x7a0bd);
  lVar13 = *param_1;
  if ((*(uint *)(lVar13 + 0x20) >> 10 & 1) == 0) {
    pcVar11 = s_link__UIC_command_fail_ffff0000907d2036;
    param_1[10] = 0;
    goto LAB_ffff000090791cb8;
  }
LAB_ffff000090791c78:
  *(undefined4 *)(lVar13 + 0x20) = 0x400;
  lVar14 = param_1[10];
  if (lVar14 != 0) {
    *(uint *)(lVar14 + 8) = *(uint *)(lVar14 + 8) | *(uint *)(lVar13 + 0x98) & 0xff;
    *(undefined4 *)(lVar14 + 0xc) = *(undefined4 *)(lVar13 + 0x9c);
  }
  param_1[10] = 0;
  pcVar11 = s_link__Device_not_present_ffff0000907faacd;
  if ((*(uint *)(lVar13 + 0x30) & 1) == 0) goto LAB_ffff000090791cb8;
  local_88 = 0;
  uStack_84 = 0;
  local_80 = 0;
  uStack_7c = 0xff;
  local_90 = (int)DAT_ffff00009081252c;
  uStack_8c = (undefined4)((ulong)DAT_ffff00009081252c >> 0x20);
  if ((int)param_1[8] < 0) {
    local_98 = local_98 & 0xffffffff;
    iVar7 = FUN_ffff00009078fe48(param_1,&local_90,1);
    (*(code *)param_1[0x47])(param_1,0xd0b50000,(long)&local_98 + 4);
    local_98 = local_98 & 0xfffffeffffffffff;
    (*(code *)param_1[0x49])(param_1,0xd0b50000);
    if (iVar7 != 0) goto LAB_ffff000090792240;
    lVar13 = *param_1;
  }
  lVar14 = param_1[7];
  *(int *)(lVar13 + 0x54) = (int)((ulong)lVar14 >> 0x20);
  *(int *)(lVar13 + 0x50) = (int)lVar14;
  *(uint *)(lVar13 + 0x24) = *(uint *)(lVar13 + 0x24) | 0x30a75;
  if ((*(uint *)(lVar13 + 0x30) & 0xfe) == 0xe) {
    *(undefined4 *)(lVar13 + 0x80) = 1;
    *(undefined4 *)(lVar13 + 0x60) = 1;
    param_1[0xb] = 0x100000001;
    param_1[0xc] = 0x100000001;
    param_1[0xd] = 0x200000002;
    *(undefined4 *)(param_1 + 0xe) = 1;
    iVar6 = (*(code *)param_1[0x42])(param_1);
    if (iVar6 != 0) {
      return iVar6;
    }
    local_90 = CONCAT31(local_90._1_3_,1);
    iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0);
    if ((((((iVar6 != 0) && (iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0)) &&
          (iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0)) &&
         ((iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0 &&
          (iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0)))) &&
        (((iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0 &&
          ((iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0 &&
           (iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0)))) &&
         (iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0)))) &&
       (iVar6 = (*(code *)param_1[0x43])(param_1,6,1,0), iVar6 != 0)) {
      return iVar6;
    }
    lVar13 = FUN_ffff0000907168b0();
    goto LAB_ffff0000907920a4;
  }
  iVar7 = -1;
LAB_ffff000090792240:
  pcVar11 = s_link_startup_failed__d_ffff0000907d399b;
  goto LAB_ffff00009079224c;
  while( true ) {
    lVar14 = FUN_ffff0000907168b0();
    if (4999999 < (ulong)(lVar14 - lVar13)) break;
LAB_ffff0000907920a4:
    iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90);
    if (((((iVar6 != 0) && (iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0)) &&
         (iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0)) &&
        (((iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0 &&
          (iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0)) &&
         ((iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0 &&
          ((iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0 &&
           (iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0)))))))) &&
       ((iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0 &&
        (iVar6 = (*(code *)param_1[0x43])(param_1,5,1,&local_90), iVar6 != 0)))) {
      return iVar6;
    }
    if ((char)local_90 == '\0') goto LAB_ffff000090792280;
  }
  if ((char)local_90 != '\0') {
    return -7;
  }
LAB_ffff000090792280:
  if ((DAT_ffff00009090a574 & 1) == 0) {
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff000090794414(param_1,1,0,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\0') {
        FUN_ffff00009077e11c
                  (s__s__descriptor_header_id__d_and_d_ffff0000907d9f93,
                   s_ufshcd_read_desc_length_ffff0000907d0499,local_98._5_1_,0);
        goto LAB_ffff0000907922fc;
      }
      DAT_ffff000090836778 = (uint)local_98._4_1_;
    }
    else {
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_descriptor_hea_ffff0000907fff49,
                 s_ufshcd_read_desc_length_ffff0000907d0499,0);
LAB_ffff0000907922fc:
      DAT_ffff000090836778 = 0x40;
    }
    FUN_ffff00009077e11c(s_device_descriptor_length___0x_x_ffff0000907dba54);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff000090794414(param_1,1,8,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\b') {
        FUN_ffff00009077e11c
                  (s__s__descriptor_header_id__d_and_d_ffff0000907d9f93,
                   s_ufshcd_read_desc_length_ffff0000907d0499,local_98._5_1_,8);
        goto LAB_ffff00009079238c;
      }
      DAT_ffff000090836798 = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_descriptor_hea_ffff0000907fff49,
                 s_ufshcd_read_desc_length_ffff0000907d0499,8);
LAB_ffff00009079238c:
      DAT_ffff000090836798 = 0x62;
    }
    FUN_ffff00009077e11c(s_power_descriptor_length___0x_x_ffff0000907d204e);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff000090794414(param_1,1,4,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\x04') {
        FUN_ffff00009077e11c
                  (s__s__descriptor_header_id__d_and_d_ffff0000907d9f93,
                   s_ufshcd_read_desc_length_ffff0000907d0499,local_98._5_1_,4);
        goto LAB_ffff00009079241c;
      }
      DAT_ffff000090836788 = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_descriptor_hea_ffff0000907fff49,
                 s_ufshcd_read_desc_length_ffff0000907d0499,4);
LAB_ffff00009079241c:
      DAT_ffff000090836788 = 6;
    }
    FUN_ffff00009077e11c(s_interconnect_descriptor_length___ffff0000907f3960);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff000090794414(param_1,1,1,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\x01') {
        FUN_ffff00009077e11c
                  (s__s__descriptor_header_id__d_and_d_ffff0000907d9f93,
                   s_ufshcd_read_desc_length_ffff0000907d0499,local_98._5_1_,1);
        goto LAB_ffff0000907924ac;
      }
      DAT_ffff00009083677c = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_descriptor_hea_ffff0000907fff49,
                 s_ufshcd_read_desc_length_ffff0000907d0499,1);
LAB_ffff0000907924ac:
      DAT_ffff00009083677c = 0x90;
    }
    FUN_ffff00009077e11c(s_configuration_descriptor_length___ffff0000907dee74);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff000090794414(param_1,1,2,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\x02') {
        FUN_ffff00009077e11c
                  (s__s__descriptor_header_id__d_and_d_ffff0000907d9f93,
                   s_ufshcd_read_desc_length_ffff0000907d0499,local_98._5_1_,2);
        goto LAB_ffff00009079253c;
      }
      DAT_ffff000090836780 = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_descriptor_hea_ffff0000907fff49,
                 s_ufshcd_read_desc_length_ffff0000907d0499,2);
LAB_ffff00009079253c:
      DAT_ffff000090836780 = 0x23;
    }
    FUN_ffff00009077e11c(s_unit_descriptor_length___0x_x_ffff0000907e215a);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff000090794414(param_1,1,7,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\a') {
        FUN_ffff00009077e11c
                  (s__s__descriptor_header_id__d_and_d_ffff0000907d9f93,
                   s_ufshcd_read_desc_length_ffff0000907d0499,local_98._5_1_,7);
        goto LAB_ffff0000907925cc;
      }
      DAT_ffff000090836794 = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff00009077e11c
                (s__s__Failed_to_get_descriptor_hea_ffff0000907fff49,
                 s_ufshcd_read_desc_length_ffff0000907d0499,7);
LAB_ffff0000907925cc:
      DAT_ffff000090836794 = 0x44;
    }
    FUN_ffff00009077e11c(s_geometry_descriptor_length___0x__ffff0000907d206e);
    DAT_ffff00009090a574 = 1;
  }
  iVar6 = FUN_ffff00009079315c(param_1);
  if (iVar6 != 0) {
    pcVar11 = s_Failed_getting_device_info__err___ffff0000907d9f6d;
    iVar7 = -1;
LAB_ffff00009079224c:
    FUN_ffff00009077e11c(pcVar11,iVar7);
    return iVar7;
  }
  FUN_ffff000090795150(param_1);
  if ((short)param_1[0x11] == 0x1ce) {
    (*(code *)param_1[0x49])(param_1,0x15a80000,6);
    (*(code *)param_1[0x49])(param_1,0x15a70000,10);
    uVar12 = *(uint *)((long)param_1 + 0x44);
  }
  else {
    uVar12 = *(uint *)((long)param_1 + 0x44);
  }
  if ((uVar12 >> 4 & 1) != 0) {
    (*(code *)param_1[0x49])(param_1,0x15a80000,10);
    uVar12 = *(uint *)((long)param_1 + 0x44);
  }
  if ((uVar12 >> 7 & 1) != 0) {
    local_90 = 0;
    local_98 = 0;
    local_a0 = (ulong)(uint)local_a0;
    iVar6 = (*(code *)param_1[0x47])(param_1,0x15aa0000,&local_90);
    if ((iVar6 == 0) &&
       (iVar6 = (*(code *)param_1[0x48])(param_1,0x15aa0000,(long)&local_98 + 4), iVar6 == 0)) {
      if (local_90 - 7U < 0xfffffffa) {
        pcVar11 = s__s__invalid_host_PA_GRANULARITY___ffff0000907eefa3;
      }
      else {
        if (0xfffffff9 < local_98._4_4_ - 7U) {
          iVar6 = (*(code *)param_1[0x47])(param_1,0x15a80000,&local_98);
          if ((iVar6 == 0) &&
             (iVar6 = (*(code *)param_1[0x48])(param_1,0x15a80000,(long)&local_a0 + 4), iVar6 == 0))
          {
            bVar2 = (&LAB_ffff000090812544)[local_98._4_4_ - 1];
            if (local_a0._4_4_ * (uint)bVar2 <
                (uint)local_98 * (byte)(&LAB_ffff000090812544)[local_90 - 1]) {
              uVar12 = 0;
              if (bVar2 != 0) {
                uVar12 = ((uint)local_98 * (byte)(&LAB_ffff000090812544)[local_90 - 1]) /
                         (uint)bVar2;
              }
              (*(code *)param_1[0x4a])(param_1,0x15a80000,uVar12 + 1);
            }
          }
          goto LAB_ffff0000907926fc;
        }
        pcVar11 = s__s__invalid_device_PA_GRANULARIT_ffff0000907e2179;
      }
      FUN_ffff00009077e11c(pcVar11,s_ufshcd_quirk_tune_host_pa_tactiv_ffff0000907d8760);
    }
  }
LAB_ffff0000907926fc:
  local_90 = 0;
  local_a0 = 0;
  local_98 = 0;
  local_a4 = 0;
  iVar6 = (*(code *)param_1[0x47])(param_1,0x15aa0000,&local_90);
  iVar5 = (*(code *)param_1[0x48])(param_1,0x15aa0000,(long)&local_98 + 4);
  iVar7 = (*(code *)param_1[0x47])(param_1,0x15a80000,&local_98);
  iVar8 = (*(code *)param_1[0x48])(param_1,0x15a80000,(long)&local_a0 + 4);
  iVar9 = (*(code *)param_1[0x47])(param_1,0x15a70000,&local_a0);
  iVar10 = (*(code *)param_1[0x48])(param_1,0x15a70000,&local_a4);
  iVar6 = iVar5 + iVar6 + iVar7 + iVar8 + iVar9 + iVar10;
  FUN_ffff00009077e11c(s__UFS__unipro_params__granularity_ffff0000907f086a,local_90);
  FUN_ffff00009077e11c(s__UFS__unipro_params__peer_granul_ffff0000907e3bdf,local_98._4_4_);
  FUN_ffff00009077e11c(s__UFS__unipro_params__pa_tactivat_ffff0000907fab09,local_98 & 0xffffffff);
  FUN_ffff00009077e11c(s__UFS__unipro_params__peer_pa_tac_ffff0000907eefc7,local_a0._4_4_);
  FUN_ffff00009077e11c(s__UFS__unipro_params__pa_h8time_0_ffff0000907e6ec9,local_a0 & 0xffffffff);
  FUN_ffff00009077e11c(s__UFS__unipro_params__peer_pa_h8t_ffff0000907fff1f,local_a4);
  if (iVar6 != 0) {
    FUN_ffff00009077e11c
              (s_unipro_params___s__dump_with_err_ffff0000907e882b,
               s_dump_unipro_params_ffff0000907fab31,iVar6);
  }
  FUN_ffff000090792c5c(param_1);
  if (*(char *)((long)param_1 + 0x8a) == '\0') {
    FUN_ffff00009077e11c(s_bug__active_num_lu_shall_not_be_0_ffff000090803231);
  }
  else {
    lVar13 = FUN_ffff0000907168b0();
    while( true ) {
      uVar4 = daif;
      uVar12 = (uint)uVar4;
      if ((uVar12 >> 7 & 1) == 0) {
        uVar15 = daif;
        daif = uVar15 | 0x80;
      }
      bVar2 = *(byte *)(param_1 + 0x2f);
      uVar3 = 1 << (ulong)(bVar2 & 0x1f);
      if ((param_1[0x10] & (long)(int)uVar3) == 0) break;
      param_1[9] = 0;
      if ((uVar12 >> 7 & 1) == 0) {
        uVar15 = daif;
        daif = uVar15 & 0xffffffffffffff7f;
      }
      lVar14 = FUN_ffff0000907168b0();
      if (5000000 < (ulong)(lVar14 - lVar13)) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_ufs_ufs_ffff0000907d6e37,0x353,&DAT_ffff0000907d2506);
      }
    }
    param_1[9] = 0;
    param_1[0x10] = param_1[0x10] | (long)(int)uVar3;
    if ((uVar12 >> 7 & 1) == 0) {
      uVar15 = daif;
      daif = uVar15 & 0xffffffffffffff7f;
    }
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_7c = 0;
    uStack_78 = 0;
    local_90 = 0;
    uStack_8c = 0;
    local_88 = 0;
    uStack_84 = 0;
    if (*(char *)((long)param_1 + 0x8a) != '\0') {
      uVar12 = 0;
      do {
        do {
          FUN_ffff00009078f564(&local_90,(uint)bVar2,uVar12);
          iVar6 = FUN_ffff00009079101c(param_1,&local_90);
        } while (iVar6 != 0);
        uVar12 = uVar12 + 1;
      } while (uVar12 < *(byte *)((long)param_1 + 0x8a));
    }
    do {
      FUN_ffff00009078f564(&local_90,bVar2,0xc4);
      iVar6 = FUN_ffff00009079101c(param_1,&local_90);
    } while (iVar6 != 0);
    *(uint *)(param_1 + 0x55) = *(uint *)(param_1 + 0x55) & 0xfffffffe;
    uVar4 = daif;
    if (((uint)uVar4 >> 7 & 1) == 0) {
      uVar15 = daif;
      daif = uVar15 | 0x80;
      param_1[9] = 0;
      param_1[0x10] = param_1[0x10] & (long)(int)~uVar3;
      uVar15 = daif;
      daif = uVar15 & 0xffffffffffffff7f;
    }
    else {
      param_1[9] = 0;
      param_1[0x10] = param_1[0x10] & (long)(int)~uVar3;
    }
  }
  lVar13 = (long)param_1 + 0x18c;
  if ((char)param_1[0x31] == '\0') {
    *(undefined8 *)((long)param_1 + 0x19c) = 0x100000001;
    *(undefined4 *)((long)param_1 + 0x1a4) = 2;
    (*(code *)param_1[0x47])(param_1,0x15810000,(long)param_1 + 0x194);
    (*(code *)param_1[0x47])(param_1,0x15610000,param_1 + 0x33);
    if ((*(int *)((long)param_1 + 0x194) == 0) || ((int)param_1[0x33] == 0)) {
LAB_ffff000090792a6c:
      FUN_ffff00009077e11c(s_Failed_getting_max_supported_pow_ffff0000907f8fb4);
      goto LAB_ffff000090792a78;
    }
    (*(code *)param_1[0x47])(param_1,0x15870000,lVar13);
    if (*(int *)((long)param_1 + 0x18c) == 0) {
      (*(code *)param_1[0x47])(param_1,0x15860000,lVar13);
      if (*(int *)((long)param_1 + 0x18c) == 0) goto LAB_ffff000090792a6c;
      *(undefined4 *)((long)param_1 + 0x19c) = 5;
    }
    (*(code *)param_1[0x48])(param_1,0x15870000,param_1 + 0x32);
    if ((int)param_1[0x32] == 0) {
      (*(code *)param_1[0x48])(param_1,0x15860000,param_1 + 0x32);
      if ((int)param_1[0x32] == 0) goto LAB_ffff000090792a6c;
      *(undefined4 *)(param_1 + 0x34) = 5;
    }
    *(undefined1 *)(param_1 + 0x31) = 1;
  }
  iVar6 = FUN_ffff000090790600(param_1,lVar13);
  if (iVar6 != 0) {
    FUN_ffff00009077e11c(s_Failed_setting_power_mode__err___ffff0000907eba42,iVar6);
  }
LAB_ffff000090792a78:
  FUN_ffff00009079306c(param_1);
  *(undefined4 *)(*param_1 + 0x18) = 0x140a;
  return 0;
}


