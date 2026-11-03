
undefined8 FUN_ffff0000be022f0c(long param_1)

{
  long lVar1;
  ushort uVar2;
  undefined4 uVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  uint *puVar7;
  long lVar8;
  uint *puVar9;
  ushort uVar10;
  ushort uVar11;
  ulong uVar12;
  uint *puVar13;
  uint local_1a0;
  uint local_19c;
  undefined4 local_198 [16];
  undefined4 local_158 [16];
  uint local_118 [16];
  uint local_d8 [16];
  ulong local_98 [4];
  uint local_74;
  undefined8 local_70;
  
  FUN_ffff0000be060ae0(&local_1a0,0,0x108);
  if (param_1 != 0) {
    iVar4 = FUN_ffff0000be055460(param_1);
    if (iVar4 != 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee24(s_Bad_DTB_header__ffff0000be0a4639);
    }
    iVar4 = FUN_ffff0000be05620c(param_1,s__i2c_common_ffff0000be0c2b43);
    if (-1 < iVar4) {
      local_98[2] = 0;
      local_98[3] = 0;
      local_98[0] = 0;
      local_98[1] = 0;
      local_70 = local_70 & 0xffffffff;
      puVar7 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar4,s_clock_source_ffff0000be0c1a0b,(long)&local_70 + 4)
      ;
      local_1a0 = 0;
      if ((0 < (int)local_70._4_4_) && (puVar7 != (uint *)0x0)) {
        uVar6 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
        local_1a0 = uVar6 >> 0x10 | uVar6 << 0x10;
      }
      local_70 = local_70 & 0xffffffff;
      puVar7 = (uint *)FUN_ffff0000be056494
                                 (param_1,iVar4,s_i2c_nr_ffff0000be0b63dd,(long)&local_70 + 4);
      local_19c = 0;
      if ((0 < (int)local_70._4_4_) && (puVar7 != (uint *)0x0)) {
        uVar6 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
        local_19c = uVar6 >> 0x10 | uVar6 << 0x10;
      }
      local_70 = local_70 & 0xffffffff;
      lVar8 = FUN_ffff0000be056494(param_1,iVar4,s_scl_gpio_ffff0000be0b0b75,(long)&local_70 + 4);
      iVar5 = local_70._4_4_;
      if (0xe < (int)local_70._4_4_) {
        iVar5 = 0xf;
      }
      if (((int)local_70._4_4_ < 1) || (lVar8 == 0)) {
        FUN_ffff0000be060ae0(local_158,0,(long)iVar5);
      }
      else {
        FUN_ffff0000be0607b0(local_158,lVar8);
      }
      local_70 = local_70 & 0xffffffff;
      lVar8 = FUN_ffff0000be056494(param_1,iVar4,s_sda_gpio_ffff0000be0bbdb0,(long)&local_70 + 4);
      iVar4 = local_70._4_4_;
      if (0xe < (int)local_70._4_4_) {
        iVar4 = 0xf;
      }
      if (((int)local_70._4_4_ < 1) || (lVar8 == 0)) {
        FUN_ffff0000be060ae0(local_198,0,(long)iVar4);
      }
      else {
        FUN_ffff0000be0607b0(local_198,lVar8);
      }
      if (local_19c < 0xf) {
        if (local_19c != 0) {
          uVar12 = 0;
          puVar7 = &DAT_ffff0000be0f2e60;
          puVar13 = local_d8;
          do {
            FUN_ffff0000be060ae0(local_98,0,0x20);
            uVar6 = FUN_ffff0000be0611dc
                              (local_98,0x20,s__s_d_s_ffff0000be0b2e26,&DAT_ffff0000be0a5950,
                               uVar12 & 0xffffffff,&DAT_ffff0000be0bad78);
            if (0x1f < uVar6) goto LAB_ffff0000be0230c0;
            iVar4 = FUN_ffff0000be05620c(param_1,local_98);
            if (-1 < iVar4) {
              local_70 = local_70 & 0xffffffff;
              puVar9 = (uint *)FUN_ffff0000be056494
                                         (param_1,iVar4,s_i2c_mode_ffff0000be0b9bcc,
                                          (long)&local_70 + 4);
              uVar6 = 0;
              if ((0 < (int)local_70._4_4_) && (puVar9 != (uint *)0x0)) {
                uVar6 = (*puVar9 & 0xff00ff00) >> 8 | (*puVar9 & 0xff00ff) << 8;
                uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
              }
              puVar13[-0x10] = uVar6;
              local_70 = local_70 & 0xffffffff;
              puVar9 = (uint *)FUN_ffff0000be056494
                                         (param_1,iVar4,s_internal_pull_up_ffff0000be0b50bf,
                                          (long)&local_70 + 4);
              uVar6 = 0;
              if ((0 < (int)local_70._4_4_) && (puVar9 != (uint *)0x0)) {
                uVar6 = (*puVar9 & 0xff00ff00) >> 8 | (*puVar9 & 0xff00ff) << 8;
                uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
              }
              *puVar13 = uVar6;
              local_70 = local_70 & 0xffffffff;
              puVar9 = (uint *)FUN_ffff0000be056494
                                         (param_1,iVar4,s_speed_ffff0000be0b0b7e,(long)&local_70 + 4
                                         );
              uVar6 = 0;
              if ((0 < (int)local_70._4_4_) && (puVar9 != (uint *)0x0)) {
                uVar6 = (*puVar9 & 0xff00ff00) >> 8 | (*puVar9 & 0xff00ff) << 8;
                uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
              }
              *puVar7 = uVar6;
              puVar7[-6] = local_1a0;
            }
            uVar12 = uVar12 + 1;
            puVar7 = puVar7 + 0x24;
            puVar13 = puVar13 + 1;
          } while (uVar12 < local_19c);
          if (local_19c != 0) {
            uVar12 = 0;
            do {
              uVar3 = local_158[uVar12];
              uVar6 = local_d8[uVar12];
              FUN_ffff0000be065ed8(uVar3,local_118[uVar12]);
              FUN_ffff0000be065fa8(uVar3,uVar6);
              if (uVar6 != 0) {
                FUN_ffff0000be066190(uVar3,1);
              }
              FUN_ffff0000be065dc4(uVar3,0);
              FUN_ffff0000be065f3c(uVar3,1);
              uVar3 = local_198[uVar12];
              uVar6 = local_d8[uVar12];
              FUN_ffff0000be065ed8(uVar3,local_118[uVar12]);
              FUN_ffff0000be065fa8(uVar3,uVar6);
              if (uVar6 != 0) {
                FUN_ffff0000be066190(uVar3,1);
              }
              FUN_ffff0000be065dc4(uVar3,0);
              FUN_ffff0000be065f3c(uVar3,1);
              uVar12 = uVar12 + 1;
            } while (uVar12 < local_19c);
          }
        }
        goto LAB_ffff0000be0230c4;
      }
    }
  }
LAB_ffff0000be0230c0:
  FUN_ffff0000be074f20();
LAB_ffff0000be0230c4:
  FUN_ffff0000be074f2c();
  iVar4 = 0;
  lVar8 = -0x870;
  do {
    local_98[0] = local_98[0] & 0xffffffff00000000;
    lVar1 = (long)&PTR_DAT_ffff0000be0f36a8 + lVar8;
    local_70 = 0;
    local_74 = 0;
    uVar6 = *(uint *)(&DAT_ffff0000be0f36d0 + lVar8);
    uVar3 = *(undefined4 *)((long)&PTR_DAT_ffff0000be0f36b8 + lVar8);
    if ((uVar6 < 0x191) || (*(char *)((long)&PTR_DAT_ffff0000be0f36e8 + lVar8 + 1) != '\0')) {
      uVar12 = (ulong)(uVar6 * 0x32) / 0x32;
      iVar5 = FUN_ffff0000be02347c(lVar1,uVar3,uVar12,local_98,(long)&local_70 + 4);
      if ((-1 < iVar5) &&
         (iVar5 = FUN_ffff0000be02347c(lVar1,uVar3,uVar12,&local_70,&local_74), -1 < iVar5)) {
        uVar10 = 0;
        uVar2 = (ushort)local_98[0] & 0x3f | (ushort)((local_70._4_4_ & 7) << 8);
        uVar11 = (ushort)local_70 & 0x3f | (ushort)((local_74 & 7) << 6);
        goto LAB_ffff0000be0231b0;
      }
LAB_ffff0000be0230f0:
      FUN_ffff0000be05fd98
                (s__s__d__bus__d_i2c_set_speed_fail_ffff0000be0b63e4,
                 s_i2c_set_bus_speed_ffff0000be0a6c7f,0x3a9,iVar4,iVar5);
    }
    else {
      iVar5 = FUN_ffff0000be02347c(lVar1,uVar3,400,&local_70,&local_74);
      if ((iVar5 < 0) ||
         (iVar5 = FUN_ffff0000be02347c(lVar1,uVar3,uVar6,local_98,(long)&local_70 + 4), iVar5 < 0))
      goto LAB_ffff0000be0230f0;
      uVar2 = (ushort)local_70 & 0x3f | (ushort)((local_74 & 7) << 8);
      uVar10 = (ushort)((local_70._4_4_ & 7) << 0xc);
      uVar11 = (ushort)(local_74 << 6) | (ushort)(((uint)local_98[0] & 7) << 9) |
               uVar10 | (ushort)local_70;
      uVar10 = uVar10 | (ushort)(((uint)local_98[0] & 7) << 8) | 3;
LAB_ffff0000be0231b0:
      *(ushort *)((long)&PTR_s_tinysys_mcupm_RV33_A_ffff0000be0f3728 + lVar8) = uVar2;
      *(ushort *)((long)&PTR_s_tinysys_mcupm_RV33_A_ffff0000be0f3728 + lVar8 + 2) = uVar11;
      *(ushort *)((long)&PTR_s_tinysys_mcupm_RV33_A_ffff0000be0f3728 + lVar8 + 4) = uVar10;
      FUN_ffff0000be0236e0(lVar1);
    }
    FUN_ffff0000be01a6f8(&DAT_ffff0000be0f36f0 + lVar8);
    lVar8 = lVar8 + 0x90;
    iVar4 = iVar4 + 1;
    if (lVar8 == 0) {
      return 0;
    }
  } while( true );
}


