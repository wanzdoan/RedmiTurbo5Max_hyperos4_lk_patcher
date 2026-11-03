
undefined8 FUN_ffff0000b8056e70(long param_1)

{
  undefined1 uVar1;
  byte bVar2;
  undefined1 uVar3;
  byte bVar4;
  ushort uVar5;
  int iVar6;
  int iVar7;
  long lVar8;
  undefined8 uVar9;
  char *pcVar10;
  long lVar11;
  long lVar12;
  uint uVar13;
  undefined1 *puVar14;
  undefined1 *puVar15;
  byte *pbVar16;
  byte bVar17;
  ulong uVar18;
  uint uVar19;
  ulong uVar20;
  uint uVar21;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined4 local_68;
  undefined2 local_64;
  
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  lVar8 = FUN_ffff0000b804bd14(0x40,0x100);
  if (lVar8 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,0xe54,
               s_ufs_temp_buf_ffff0000b8085b07);
  }
  if ((*(short *)(param_1 + 0x88) == 0) || (*(char *)(param_1 + 0x8e) != '\x01')) {
    FUN_ffff0000b804d070((short *)(param_1 + 0x88),0,0xf0);
    iVar6 = (**(code **)(param_1 + 0x228))(param_1,0,0,0,lVar8,DAT_ffff0000b81063e8);
    if (iVar6 == 0) {
      puVar14 = (undefined1 *)(lVar8 + 0x10);
      *(ushort *)(param_1 + 0x8c) = CONCAT11(*puVar14,*(undefined1 *)(lVar8 + 0x11));
      FUN_ffff0000b804c33c(s__UFS__UFS_version__x__x_ffff0000b809deef);
      FUN_ffff0000b804c33c
                (s__UFS___s__Device_Desc__ffff0000b8096ae6,s_ufs_get_device_info_ffff0000b8089670);
      uVar18 = 0;
      uVar20 = (ulong)DAT_ffff0000b81063e8;
      do {
        if (uVar20 <= uVar18) break;
        lVar11 = 0;
        do {
          FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*(undefined1 *)(lVar8 + uVar18 + lVar11));
          lVar12 = lVar11 + 1;
          if (uVar20 <= lVar12 + uVar18) break;
          uVar13 = (uint)lVar11;
          lVar11 = lVar12;
        } while (uVar13 < 0xf);
        uVar18 = (ulong)(uint)((int)uVar18 + (int)lVar12);
        FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
      } while (uVar18 < uVar20);
      uVar5 = *(ushort *)(lVar8 + 0x18);
      uVar13 = 0;
      bVar4 = 0;
      *(bool *)(param_1 + 0x8e) = *(char *)(lVar8 + 8) != '\0';
      bVar17 = 1;
      *(ushort *)(param_1 + 0x88) = uVar5 >> 8 | uVar5 << 8;
LAB_ffff0000b805706c:
      if (*(char *)(lVar8 + 0x1f) < '\0') {
        uVar5 = *(ushort *)(lVar8 + 0x40);
        uVar3 = *(undefined1 *)(lVar8 + 0x42);
        *(undefined1 *)(param_1 + 0x129) = 1;
        *(undefined1 *)(param_1 + 300) = uVar3;
        *(ushort *)(param_1 + 0x12a) = uVar5 >> 8 | uVar5 << 8;
        if ((*(byte *)(lVar8 + 0x51) & 1) == 0) goto LAB_ffff0000b8057080;
LAB_ffff0000b80570b0:
        uVar3 = *(undefined1 *)(lVar8 + 0x53);
        uVar1 = *(undefined1 *)(lVar8 + 0x54);
        *(undefined1 *)(param_1 + 0x12d) = 1;
        *(undefined1 *)(param_1 + 0x12e) = uVar3;
        *(undefined1 *)(param_1 + 0x12f) = uVar1;
        FUN_ffff0000b804c33c(s_TW_Reduction____d__TW_Type____d_ffff0000b8091eca);
        if ((bool)(*(short *)(param_1 + 0x88) != 0x1ce | bVar4 ^ 1)) {
          *(uint *)(param_1 + 0x134) =
               (uint)*(byte *)(lVar8 + 0x55) << 0x18 | (uint)*(byte *)(lVar8 + 0x56) << 0x10 |
               (uint)*(byte *)(lVar8 + 0x57) << 5 | (uint)*(byte *)(lVar8 + 0x58);
          FUN_ffff0000b804c33c(s_Shared_WB_Buffer_AU___0x_x_ffff0000b809a2a6);
        }
        else {
          puVar15 = (undefined1 *)(lVar8 + 0x55);
          pbVar16 = (byte *)(lVar8 + 0x56);
          if ((*(short *)(param_1 + 0x8c) != 0x210) && (*(short *)(param_1 + 0x8c) != 0x300)) {
            puVar15 = (undefined1 *)(lVar8 + 0x4d);
            pbVar16 = (byte *)(lVar8 + 0x4e);
          }
          bVar2 = *pbVar16;
          uVar3 = *puVar15;
          *(ushort *)(param_1 + 0x130) = CONCAT11(uVar3,bVar2);
          FUN_ffff0000b804c33c
                    (s_TW_Version____x__x__x_ffff0000b8085b4f,uVar3,bVar2 >> 4,bVar2 & 0xf);
        }
      }
      else {
        *(undefined1 *)(param_1 + 0x129) = 0;
        if ((*(byte *)(lVar8 + 0x51) & 1) != 0) goto LAB_ffff0000b80570b0;
LAB_ffff0000b8057080:
        *(undefined1 *)(param_1 + 0x12d) = 0;
      }
      uVar3 = *(undefined1 *)(lVar8 + 0x1b);
      *(undefined1 *)(param_1 + 0x127) = *(undefined1 *)(lVar8 + 0x1a);
      *(undefined1 *)(param_1 + 0x128) = uVar3;
      FUN_ffff0000b804c33c(s_ud0_base_offset____d__ud_config__ffff0000b80a160d);
      if (*(short *)(param_1 + 0x88) == 0x1ce && !(bool)(bVar17 ^ 1)) {
        local_64 = 0;
        local_68 = 2;
        iVar7 = FUN_ffff0000b8058128(param_1,1,0,0,1,&local_64,&local_68);
        if (iVar7 == 0) {
          if (local_64._1_1_ == '\0') {
            iVar7 = 0;
          }
          else {
            FUN_ffff0000b804c33c
                      (s__s__descriptor_header_id__d_and_d_ffff0000b808b140,
                       s_ufshcd_read_desc_length_ffff0000b8085bd1,local_64._1_1_,0);
            iVar7 = -1;
          }
          uVar13 = (uint)(byte)local_64;
        }
        else {
          FUN_ffff0000b804c33c
                    (s__s__Failed_to_get_descriptor_hea_ffff0000b809fdc6,
                     s_ufshcd_read_desc_length_ffff0000b8085bd1,0);
        }
        iVar6 = (**(code **)(param_1 + 0x228))(param_1,0,0,1,lVar8,uVar13);
        iVar6 = iVar6 + iVar7;
        if (iVar6 != 0) {
          pcVar10 = s__UFS__err___s__failed_reading_HP_ffff0000b8092d17;
          goto LAB_ffff0000b8056f20;
        }
        uVar19 = 0;
        do {
          if (uVar13 <= uVar19) break;
          lVar11 = 0;
          do {
            FUN_ffff0000b804c33c
                      (&DAT_ffff0000b808875a,*(undefined1 *)(lVar8 + (ulong)uVar19 + lVar11));
            lVar12 = lVar11 + 1;
            if ((ulong)uVar13 <= lVar12 + (ulong)uVar19) break;
            uVar21 = (uint)lVar11;
            lVar11 = lVar12;
          } while (uVar21 < 0xf);
          uVar19 = uVar19 + (int)lVar12;
          FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
        } while (uVar19 < uVar13);
        if (-1 < *(char *)(lVar8 + 0x1f)) {
          bVar4 = 0;
          goto LAB_ffff0000b80574a4;
        }
        if ((DAT_ffff0000b81b6938 & 1) == 0) {
          local_64 = 0;
          local_68 = 2;
          iVar6 = FUN_ffff0000b8058128(param_1,1,0,0,1,&local_64,&local_68);
          if (iVar6 == 0) {
            if (local_64._1_1_ == '\0') {
              DAT_ffff0000b81063e8 = (uint)(byte)local_64;
              FUN_ffff0000b804c33c(s_vendor_device_descriptor_length___ffff0000b8094dd5);
              local_64 = 0;
              local_68 = 2;
              iVar6 = FUN_ffff0000b8058128(param_1,1,1,0,1,&local_64,&local_68);
              if (iVar6 == 0) {
                if (local_64._1_1_ == '\x01') {
                  DAT_ffff0000b81063ec = (uint)(byte)local_64;
                  FUN_ffff0000b804c33c(s_vendor_configuration_descriptor_l_ffff0000b8094dfd);
                  local_64 = 0;
                  local_68 = 2;
                  iVar6 = FUN_ffff0000b8058128(param_1,1,7,0,1,&local_64,&local_68);
                  if (iVar6 == 0) {
                    if (local_64._1_1_ == '\a') {
                      DAT_ffff0000b8106404 = (uint)(byte)local_64;
                      FUN_ffff0000b804c33c(s_vendor_geometry_descriptor_lengt_ffff0000b8098687);
                      iVar6 = 0;
                    }
                    else {
                      FUN_ffff0000b804c33c
                                (s__s__descriptor_header_id__d_and_d_ffff0000b808b140,
                                 s_ufshcd_read_desc_length_ffff0000b8085bd1,local_64._1_1_,7);
                      iVar6 = -1;
                      DAT_ffff0000b8106404 = (uint)(byte)local_64;
                    }
                  }
                  else {
                    FUN_ffff0000b804c33c
                              (s__s__Failed_to_get_descriptor_hea_ffff0000b809fdc6,
                               s_ufshcd_read_desc_length_ffff0000b8085bd1,7);
                  }
                }
                else {
                  FUN_ffff0000b804c33c
                            (s__s__descriptor_header_id__d_and_d_ffff0000b808b140,
                             s_ufshcd_read_desc_length_ffff0000b8085bd1,local_64._1_1_,1);
                  iVar6 = -1;
                  DAT_ffff0000b81063ec = (uint)(byte)local_64;
                }
              }
              else {
                FUN_ffff0000b804c33c
                          (s__s__Failed_to_get_descriptor_hea_ffff0000b809fdc6,
                           s_ufshcd_read_desc_length_ffff0000b8085bd1,1);
              }
            }
            else {
              FUN_ffff0000b804c33c
                        (s__s__descriptor_header_id__d_and_d_ffff0000b808b140,
                         s_ufshcd_read_desc_length_ffff0000b8085bd1,local_64._1_1_,0);
              iVar6 = -1;
              DAT_ffff0000b81063e8 = (uint)(byte)local_64;
            }
          }
          else {
            FUN_ffff0000b804c33c
                      (s__s__Failed_to_get_descriptor_hea_ffff0000b809fdc6,
                       s_ufshcd_read_desc_length_ffff0000b8085bd1,0);
          }
          DAT_ffff0000b81b6938 = 1;
        }
        else {
          iVar6 = 0;
        }
        bVar17 = 0;
        bVar4 = 1;
        if (iVar6 != 0) goto LAB_ffff0000b8056f2c;
        goto LAB_ffff0000b805706c;
      }
LAB_ffff0000b80574a4:
      *(byte *)(param_1 + 0x1a8) = bVar4;
      lVar11 = FUN_ffff0000b8009fb4();
      while( true ) {
        uVar9 = daif;
        uVar13 = (uint)uVar9;
        if ((uVar13 >> 7 & 1) == 0) {
          uVar18 = daif;
          daif = uVar18 | 0x80;
        }
        bVar4 = *(byte *)(param_1 + 0x178);
        uVar18 = (ulong)(1 << (ulong)(bVar4 & 0x1f));
        if ((*(ulong *)(param_1 + 0x80) & uVar18) == 0) break;
        *(undefined8 *)(param_1 + 0x48) = 0;
        if ((uVar13 >> 7 & 1) == 0) {
          uVar18 = daif;
          daif = uVar18 & 0xffffffffffffff7f;
        }
        lVar12 = FUN_ffff0000b8009fb4();
        if (5000000 < (ulong)(lVar12 - lVar11)) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000b804b830
                    (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,0x353,&DAT_ffff0000b8086efb
                    );
        }
      }
      *(undefined8 *)(param_1 + 0x48) = 0;
      *(ulong *)(param_1 + 0x80) = *(ulong *)(param_1 + 0x80) | uVar18;
      if ((uVar13 >> 7 & 1) == 0) {
        uVar18 = daif;
        daif = uVar18 & 0xffffffffffffff7f;
      }
      FUN_ffff0000b804d070(lVar8,0,0x2d);
      FUN_ffff0000b8053264(&local_98,(uint)bVar4,0,lVar8,0,0);
      iVar6 = FUN_ffff0000b8054db0(param_1,&local_98);
      if (iVar6 != 0) goto LAB_ffff0000b8056f2c;
      iVar6 = FUN_ffff0000b805a49c(puVar14,0x10);
      if (iVar6 != 0) {
        FUN_ffff0000b804cd40(param_1 + 0x8f,puVar14,(long)iVar6);
      }
      iVar6 = FUN_ffff0000b805a49c(lVar8 + 0x20,4);
      if (iVar6 != 0) {
        FUN_ffff0000b804cd40(param_1 + 0xa0,lVar8 + 0x20,(long)iVar6);
      }
      FUN_ffff0000b804d070(lVar8,0,0x2d);
      FUN_ffff0000b8053264(&local_98,bVar4,0,lVar8,1,0);
      iVar6 = FUN_ffff0000b8054db0(param_1,&local_98);
      if (iVar6 != 0) {
LAB_ffff0000b80575e0:
        FUN_ffff0000b804c33c(s__UFS__err__ufshcd_queuecommand_e_ffff0000b8095b53);
        goto LAB_ffff0000b8056f2c;
      }
      if (4 < (ulong)*(byte *)(lVar8 + 3)) {
        uVar18 = 4;
        do {
          if (*(char *)(lVar8 + uVar18) == -0x80) {
            FUN_ffff0000b804d070(lVar8,0,0x2d);
            FUN_ffff0000b8053264(&local_98,bVar4,0,lVar8,1,0x80);
            iVar6 = FUN_ffff0000b8054db0(param_1,&local_98);
            if (iVar6 != 0) goto LAB_ffff0000b80575e0;
            iVar6 = FUN_ffff0000b805a49c(lVar8 + 4,*(undefined1 *)(lVar8 + 3));
            *(char *)(param_1 + 0x126) = (char)iVar6;
            if (iVar6 != 0) {
              FUN_ffff0000b804c33c(s__UFS__get_unit_serial_number_str_ffff0000b8099427,iVar6);
              FUN_ffff0000b804cd40(param_1 + 0xa5,lVar8 + 4,(long)iVar6);
            }
            break;
          }
          uVar18 = uVar18 + 1;
        } while (*(byte *)(lVar8 + 3) != uVar18);
      }
      FUN_ffff0000b804d070(lVar8,0,DAT_ffff0000b81063f0);
      iVar6 = (**(code **)(param_1 + 0x228))(param_1,2,0xc4,0,lVar8,DAT_ffff0000b81063f0);
      if (iVar6 != 0) {
        pcVar10 = s__UFS__err___s__failed_reading_RP_ffff0000b808d741;
        goto LAB_ffff0000b8056f20;
      }
      uVar13 = (uint)*(byte *)(lVar8 + 10);
      if (*(byte *)(lVar8 + 10) == 0) {
        uVar19 = 1;
      }
      else {
        uVar19 = 1;
        do {
          uVar19 = uVar19 << 1;
          uVar13 = uVar13 - 1;
        } while (uVar13 != 0);
      }
      FUN_ffff0000b804c33c(s__UFS__rpmb__logical_blk_size___d_ffff0000b8094d1a,uVar19);
      uVar18 = (*(ulong *)(lVar8 + 0xb) & 0xff00ff00ff00ff00) >> 8 |
               (*(ulong *)(lVar8 + 0xb) & 0xff00ff00ff00ff) << 8;
      uVar18 = (uVar18 & 0xffff0000ffff0000) >> 0x10 | (uVar18 & 0xffff0000ffff) << 0x10;
      uVar18 = uVar18 >> 0x20 | uVar18 << 0x20;
      FUN_ffff0000b804c33c(s__UFS__rpmb__logical_blk_cnt__0x__ffff0000b809df08,uVar18);
      bVar17 = *(byte *)(lVar8 + 0x13);
      *(ulong *)(param_1 + 0x140) = uVar18 * uVar19;
      *(ulong *)(param_1 + 0x150) = (ulong)bVar17 << 0x11;
      FUN_ffff0000b804c33c(s__UFS__rpmb__region__d___0x_llx_ffff0000b808f5ab,0);
      *(ulong *)(param_1 + 0x158) = (ulong)*(byte *)(lVar8 + 0x14) << 0x11;
      FUN_ffff0000b804c33c(s__UFS__rpmb__region__d___0x_llx_ffff0000b808f5ab,1);
      *(ulong *)(param_1 + 0x160) = (ulong)*(byte *)(lVar8 + 0x15) << 0x11;
      FUN_ffff0000b804c33c(s__UFS__rpmb__region__d___0x_llx_ffff0000b808f5ab,2);
      *(ulong *)(param_1 + 0x168) = (ulong)*(byte *)(lVar8 + 0x16) << 0x11;
      FUN_ffff0000b804c33c(s__UFS__rpmb__region__d___0x_llx_ffff0000b808f5ab,3);
      FUN_ffff0000b804d070(lVar8,0,DAT_ffff0000b8106404);
      iVar6 = (**(code **)(param_1 + 0x228))(param_1,7,0,0,lVar8,DAT_ffff0000b8106404);
      if (iVar6 != 0) {
        pcVar10 = s__UFS__err___s__failed_reading_GE_ffff0000b8093d60;
        goto LAB_ffff0000b8056f20;
      }
      *(uint *)(param_1 + 0x148) = (uint)*(byte *)(lVar8 + 0x17);
      FUN_ffff0000b804c33c(s__UFS__rpmb__read_write_size___d_ffff0000b8085b66);
      FUN_ffff0000b8053dcc(param_1,bVar4);
      goto LAB_ffff0000b8056ed0;
    }
    pcVar10 = s__UFS__err___s__failed_reading_De_ffff0000b809cf56;
LAB_ffff0000b8056f20:
    FUN_ffff0000b804c33c(pcVar10,s_ufs_get_device_info_ffff0000b8089670,iVar6);
LAB_ffff0000b8056f2c:
    thunk_FUN_ffff0000b8082558(lVar8);
    uVar9 = 0xffffffff;
  }
  else {
LAB_ffff0000b8056ed0:
    thunk_FUN_ffff0000b8082558(lVar8);
    uVar9 = 0;
  }
  return uVar9;
}


