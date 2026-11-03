
int FUN_ffff0000be06f4f4(long *param_1)

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
  
  uVar4 = DAT_ffff0000be0d25dc;
  iVar6 = 3;
LAB_ffff0000be06f564:
  local_98 = local_98 & 0xffffffff00000000;
  local_88 = 0;
  uStack_84 = 0;
  local_80 = 0;
  uStack_7c = 0xff;
  local_90 = (int)uVar4;
  uStack_8c = (undefined4)((ulong)uVar4 >> 0x20);
  FUN_ffff0000be04c778();
  if ((int)param_1[8] < 0) {
    *(uint *)(*param_1 + 0x24) = *(uint *)(*param_1 + 0x24) | 0x400;
    FUN_ffff0000be06d968(param_1,&local_90,1);
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
    pcVar11 = s_link__UIC_command_fail_ffff0000be0a5111;
LAB_ffff0000be06f758:
    FUN_ffff0000be05fd98(pcVar11);
    FUN_ffff0000be07488c(param_1,0,0xa0);
    FUN_ffff0000be07488c(param_1,0x100,0x74);
    local_90 = 0;
    (*(code *)param_1[0x47])(param_1,0xd0000000,&local_90);
    FUN_ffff0000be05fd98(s__UFS__DME_LayerEnable_0x_x____0x_ffff0000be0ba684,0xd000,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0200000,&local_90);
    FUN_ffff0000be05fd98(s__UFS__DME_LinkStartup_0x_x____0x_ffff0000be0af0e3,0xd020,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0600000,&local_90);
    FUN_ffff0000be05fd98(s__UFS__DME_ErrorPHYInd_0x_x____0x_ffff0000be0a3db4,0xd060,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0610000,&local_90);
    FUN_ffff0000be05fd98(s__UFS__DME_ErrorPAInd_0x_x____0x__ffff0000be0ba6a8,0xd061,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0830000,&local_90);
    FUN_ffff0000be05fd98(s__UFS__PowerState_0x_x____0x_x_ffff0000be0a9a8b,0xd083,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0950000,&local_90);
    FUN_ffff0000be05fd98(s__UFS__DebugLinkStartup_0x_x____0_ffff0000be0b27cf,0xd095,local_90);
    (*(code *)param_1[0x47])(param_1,0xd0970000,&local_90);
    FUN_ffff0000be05fd98(s__UFS__DebugStates_0x_x____0x_x_ffff0000be0bee00,0xd097,local_90);
    (*(code *)param_1[0x47])(param_1,0x410000,&local_90);
    FUN_ffff0000be05fd98(s__UFS__TX0_FSM_State_0x_x____0x_x_ffff0000be0c13c0,0x41,local_90);
    (*(code *)param_1[0x47])(param_1,0x410001,&local_90);
    FUN_ffff0000be05fd98(s__UFS__TX1_FSM_State_0x_x____0x_x_ffff0000be0b810d,0x41,local_90);
    (*(code *)param_1[0x47])(param_1,0xc10004,&local_90);
    FUN_ffff0000be05fd98(s__UFS__RX0_FSM_State_0x_x____0x_x_ffff0000be0c6b38,0xc1,local_90);
    (*(code *)param_1[0x47])(param_1,0xc10005,&local_90);
    FUN_ffff0000be05fd98(s__UFS__RX1_FSM_State_0x_x____0x_x_ffff0000be0b933f,0xc1,local_90);
    FUN_ffff0000be04c7b8(param_1);
    FUN_ffff0000be04cacc();
    FUN_ffff0000be05fd98(s_link_startup_fail__retrying_the___ffff0000be0b05a6,4 - iVar6);
    iVar5 = FUN_ffff0000be06f39c(param_1);
    iVar7 = -1;
    if ((iVar5 != 0) || (bVar1 = iVar6 == 0, iVar6 = iVar6 + -1, bVar1)) goto LAB_ffff0000be06fce0;
    goto LAB_ffff0000be06f564;
  }
  uVar15 = 0xffffffffffffff9c;
  param_1[10] = (long)&local_90;
  *(undefined4 *)(lVar13 + 0x94) = 0;
  *(undefined4 *)(lVar13 + 0x98) = 0;
  *(undefined4 *)(lVar13 + 0x9c) = 0;
  *(undefined4 *)(lVar13 + 0x90) = 0x16;
  do {
    lVar13 = *param_1;
    if ((*(uint *)(lVar13 + 0x20) >> 10 & 1) != 0) goto LAB_ffff0000be06f718;
    FUN_ffff0000be05ede4(100);
    uVar15 = uVar15 + 100;
  } while (uVar15 < 0x7a0bd);
  lVar13 = *param_1;
  if ((*(uint *)(lVar13 + 0x20) >> 10 & 1) == 0) {
    pcVar11 = s_link__UIC_command_fail_ffff0000be0a5111;
    param_1[10] = 0;
    goto LAB_ffff0000be06f758;
  }
LAB_ffff0000be06f718:
  *(undefined4 *)(lVar13 + 0x20) = 0x400;
  lVar14 = param_1[10];
  if (lVar14 != 0) {
    *(uint *)(lVar14 + 8) = *(uint *)(lVar14 + 8) | *(uint *)(lVar13 + 0x98) & 0xff;
    *(undefined4 *)(lVar14 + 0xc) = *(undefined4 *)(lVar13 + 0x9c);
  }
  param_1[10] = 0;
  pcVar11 = s_link__Device_not_present_ffff0000be0c13a6;
  if ((*(uint *)(lVar13 + 0x30) & 1) == 0) goto LAB_ffff0000be06f758;
  local_88 = 0;
  uStack_84 = 0;
  local_80 = 0;
  uStack_7c = 0xff;
  local_90 = (int)DAT_ffff0000be0d25f4;
  uStack_8c = (undefined4)((ulong)DAT_ffff0000be0d25f4 >> 0x20);
  if ((int)param_1[8] < 0) {
    local_98 = local_98 & 0xffffffff;
    iVar7 = FUN_ffff0000be06d968(param_1,&local_90,1);
    (*(code *)param_1[0x47])(param_1,0xd0b50000,(long)&local_98 + 4);
    local_98 = local_98 & 0xfffffeffffffffff;
    (*(code *)param_1[0x49])(param_1,0xd0b50000);
    if (iVar7 != 0) goto LAB_ffff0000be06fce0;
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
    lVar13 = FUN_ffff0000be0198a8();
    goto LAB_ffff0000be06fb44;
  }
  iVar7 = -1;
LAB_ffff0000be06fce0:
  pcVar11 = s_link_startup_failed__d_ffff0000be0a644a;
  goto LAB_ffff0000be06fcec;
  while( true ) {
    lVar14 = FUN_ffff0000be0198a8();
    if (4999999 < (ulong)(lVar14 - lVar13)) break;
LAB_ffff0000be06fb44:
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
    if ((char)local_90 == '\0') goto LAB_ffff0000be06fd20;
  }
  if ((char)local_90 != '\0') {
    return -7;
  }
LAB_ffff0000be06fd20:
  if ((DAT_ffff0000be1b4bf4 & 1) == 0) {
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff0000be071d98(param_1,1,0,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\0') {
        FUN_ffff0000be05fd98
                  (s__s__descriptor_header_id__d_and_d_ffff0000be0aade8,
                   s_ufshcd_read_desc_length_ffff0000be0a3dd8,local_98._5_1_,0);
        goto LAB_ffff0000be06fd9c;
      }
      DAT_ffff0000be0f2d70 = (uint)local_98._4_1_;
    }
    else {
      FUN_ffff0000be05fd98
                (s__s__Failed_to_get_descriptor_hea_ffff0000be0c496e,
                 s_ufshcd_read_desc_length_ffff0000be0a3dd8,0);
LAB_ffff0000be06fd9c:
      DAT_ffff0000be0f2d70 = 0x40;
    }
    FUN_ffff0000be05fd98(s_device_descriptor_length___0x_x_ffff0000be0abea6);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff0000be071d98(param_1,1,8,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\b') {
        FUN_ffff0000be05fd98
                  (s__s__descriptor_header_id__d_and_d_ffff0000be0aade8,
                   s_ufshcd_read_desc_length_ffff0000be0a3dd8,local_98._5_1_,8);
        goto LAB_ffff0000be06fe2c;
      }
      DAT_ffff0000be0f2d90 = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff0000be05fd98
                (s__s__Failed_to_get_descriptor_hea_ffff0000be0c496e,
                 s_ufshcd_read_desc_length_ffff0000be0a3dd8,8);
LAB_ffff0000be06fe2c:
      DAT_ffff0000be0f2d90 = 0x62;
    }
    FUN_ffff0000be05fd98(s_power_descriptor_length___0x_x_ffff0000be0a5129);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff0000be071d98(param_1,1,4,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\x04') {
        FUN_ffff0000be05fd98
                  (s__s__descriptor_header_id__d_and_d_ffff0000be0aade8,
                   s_ufshcd_read_desc_length_ffff0000be0a3dd8,local_98._5_1_,4);
        goto LAB_ffff0000be06febc;
      }
      DAT_ffff0000be0f2d80 = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff0000be05fd98
                (s__s__Failed_to_get_descriptor_hea_ffff0000be0c496e,
                 s_ufshcd_read_desc_length_ffff0000be0a3dd8,4);
LAB_ffff0000be06febc:
      DAT_ffff0000be0f2d80 = 6;
    }
    FUN_ffff0000be05fd98(s_interconnect_descriptor_length___ffff0000be0bc7d2);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff0000be071d98(param_1,1,1,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\x01') {
        FUN_ffff0000be05fd98
                  (s__s__descriptor_header_id__d_and_d_ffff0000be0aade8,
                   s_ufshcd_read_desc_length_ffff0000be0a3dd8,local_98._5_1_,1);
        goto LAB_ffff0000be06ff4c;
      }
      DAT_ffff0000be0f2d74 = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff0000be05fd98
                (s__s__Failed_to_get_descriptor_hea_ffff0000be0c496e,
                 s_ufshcd_read_desc_length_ffff0000be0a3dd8,1);
LAB_ffff0000be06ff4c:
      DAT_ffff0000be0f2d74 = 0x90;
    }
    FUN_ffff0000be05fd98(s_configuration_descriptor_length___ffff0000be0adfd1);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff0000be071d98(param_1,1,2,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\x02') {
        FUN_ffff0000be05fd98
                  (s__s__descriptor_header_id__d_and_d_ffff0000be0aade8,
                   s_ufshcd_read_desc_length_ffff0000be0a3dd8,local_98._5_1_,2);
        goto LAB_ffff0000be06ffdc;
      }
      DAT_ffff0000be0f2d78 = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff0000be05fd98
                (s__s__Failed_to_get_descriptor_hea_ffff0000be0c496e,
                 s_ufshcd_read_desc_length_ffff0000be0a3dd8,2);
LAB_ffff0000be06ffdc:
      DAT_ffff0000be0f2d78 = 0x23;
    }
    FUN_ffff0000be05fd98(s_unit_descriptor_length___0x_x_ffff0000be0b05d4);
    local_98._0_6_ = (uint6)(uint)local_98;
    local_90 = 2;
    iVar6 = FUN_ffff0000be071d98(param_1,1,7,0,0,(long)&local_98 + 4,&local_90);
    if (iVar6 == 0) {
      if (local_98._5_1_ != '\a') {
        FUN_ffff0000be05fd98
                  (s__s__descriptor_header_id__d_and_d_ffff0000be0aade8,
                   s_ufshcd_read_desc_length_ffff0000be0a3dd8,local_98._5_1_,7);
        goto LAB_ffff0000be07006c;
      }
      DAT_ffff0000be0f2d8c = (uint)(local_98 >> 0x20) & 0xff;
    }
    else {
      FUN_ffff0000be05fd98
                (s__s__Failed_to_get_descriptor_hea_ffff0000be0c496e,
                 s_ufshcd_read_desc_length_ffff0000be0a3dd8,7);
LAB_ffff0000be07006c:
      DAT_ffff0000be0f2d8c = 0x44;
    }
    FUN_ffff0000be05fd98(s_geometry_descriptor_length___0x__ffff0000be0a5149);
    DAT_ffff0000be1b4bf4 = 1;
  }
  iVar6 = FUN_ffff0000be070ae0(param_1);
  if (iVar6 != 0) {
    pcVar11 = s_Failed_getting_device_info__err___ffff0000be0aadc2;
    iVar7 = -1;
LAB_ffff0000be06fcec:
    FUN_ffff0000be05fd98(pcVar11,iVar7);
    return iVar7;
  }
  FUN_ffff0000be072948(param_1);
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
        pcVar11 = s__s__invalid_host_PA_GRANULARITY___ffff0000be0b9361;
      }
      else {
        if (0xfffffff9 < local_98._4_4_ - 7U) {
          iVar6 = (*(code *)param_1[0x47])(param_1,0x15a80000,&local_98);
          if ((iVar6 == 0) &&
             (iVar6 = (*(code *)param_1[0x48])(param_1,0x15a80000,(long)&local_a0 + 4), iVar6 == 0))
          {
            bVar2 = (&LAB_ffff0000be0d260c)[local_98._4_4_ - 1];
            if (local_a0._4_4_ * (uint)bVar2 <
                (uint)local_98 * (byte)(&LAB_ffff0000be0d260c)[local_90 - 1]) {
              uVar12 = 0;
              if (bVar2 != 0) {
                uVar12 = ((uint)local_98 * (byte)(&LAB_ffff0000be0d260c)[local_90 - 1]) /
                         (uint)bVar2;
              }
              (*(code *)param_1[0x4a])(param_1,0x15a80000,uVar12 + 1);
            }
          }
          goto LAB_ffff0000be07019c;
        }
        pcVar11 = s__s__invalid_device_PA_GRANULARIT_ffff0000be0b05f3;
      }
      FUN_ffff0000be05fd98(pcVar11,s_ufshcd_quirk_tune_host_pa_tactiv_ffff0000be0a9aaa);
    }
  }
LAB_ffff0000be07019c:
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
  FUN_ffff0000be05fd98(s__UFS__unipro_params__granularity_ffff0000be0ba6cb,local_90);
  FUN_ffff0000be05fd98(s__UFS__unipro_params__peer_granul_ffff0000be0b15b4,local_98._4_4_);
  FUN_ffff0000be05fd98(s__UFS__unipro_params__pa_tactivat_ffff0000be0c13e2,local_98 & 0xffffffff);
  FUN_ffff0000be05fd98(s__UFS__unipro_params__peer_pa_tac_ffff0000be0b9385,local_a0._4_4_);
  FUN_ffff0000be05fd98(s__UFS__unipro_params__pa_h8time_0_ffff0000be0b374f,local_a0 & 0xffffffff);
  FUN_ffff0000be05fd98(s__UFS__unipro_params__peer_pa_h8t_ffff0000be0c4944,local_a4);
  if (iVar6 != 0) {
    FUN_ffff0000be05fd98
              (s_unipro_params___s__dump_with_err_ffff0000be0b4a8a,
               s_dump_unipro_params_ffff0000be0c140a,iVar6);
  }
  FUN_ffff0000be0706fc(param_1);
  if (*(char *)((long)param_1 + 0x8a) == '\0') {
    FUN_ffff0000be05fd98(s_bug__active_num_lu_shall_not_be_0_ffff0000be0c6b5a);
  }
  else {
    lVar13 = FUN_ffff0000be0198a8();
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
      lVar14 = FUN_ffff0000be0198a8();
      if (5000000 < (ulong)(lVar14 - lVar13)) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000be05ee98
                  (s_platform_mediatek_common_ufs_ufs_ffff0000be0a8870,0x353,&DAT_ffff0000be0a553d);
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
          FUN_ffff0000be06d0f0(&local_90,(uint)bVar2,uVar12);
          iVar6 = FUN_ffff0000be06eb3c(param_1,&local_90);
        } while (iVar6 != 0);
        uVar12 = uVar12 + 1;
      } while (uVar12 < *(byte *)((long)param_1 + 0x8a));
    }
    do {
      FUN_ffff0000be06d0f0(&local_90,bVar2,0xc4);
      iVar6 = FUN_ffff0000be06eb3c(param_1,&local_90);
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
LAB_ffff0000be07050c:
      FUN_ffff0000be05fd98(s_Failed_getting_max_supported_pow_ffff0000be0c00ed);
      goto LAB_ffff0000be070518;
    }
    (*(code *)param_1[0x47])(param_1,0x15870000,lVar13);
    if (*(int *)((long)param_1 + 0x18c) == 0) {
      (*(code *)param_1[0x47])(param_1,0x15860000,lVar13);
      if (*(int *)((long)param_1 + 0x18c) == 0) goto LAB_ffff0000be07050c;
      *(undefined4 *)((long)param_1 + 0x19c) = 5;
    }
    (*(code *)param_1[0x48])(param_1,0x15870000,param_1 + 0x32);
    if ((int)param_1[0x32] == 0) {
      (*(code *)param_1[0x48])(param_1,0x15860000,param_1 + 0x32);
      if ((int)param_1[0x32] == 0) goto LAB_ffff0000be07050c;
      *(undefined4 *)(param_1 + 0x34) = 5;
    }
    *(undefined1 *)(param_1 + 0x31) = 1;
  }
  iVar6 = FUN_ffff0000be06e120(param_1,lVar13);
  if (iVar6 != 0) {
    FUN_ffff0000be05fd98(s_Failed_setting_power_mode__err___ffff0000be0b6f38,iVar6);
  }
LAB_ffff0000be070518:
  FUN_ffff0000be0709f0(param_1);
  *(undefined4 *)(*param_1 + 0x18) = 0x140a;
  return 0;
}


