
uint FUN_ffff0000b804ea14(undefined8 param_1,long param_2)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  int iVar6;
  long lVar7;
  long lVar8;
  long lVar9;
  undefined8 uVar10;
  char *pcVar11;
  ulong uVar12;
  long lVar13;
  uint uVar14;
  int iVar15;
  uint uVar16;
  uint uVar17;
  ulong uVar18;
  uint uVar19;
  undefined8 local_218;
  undefined8 uStack_210;
  undefined8 local_208;
  undefined5 uStack_200;
  undefined3 local_1fb;
  undefined5 uStack_1f8;
  undefined8 local_1f0;
  undefined8 uStack_1e8;
  undefined8 local_1e0;
  undefined8 local_1d8;
  undefined8 local_1d0;
  undefined8 local_1c8;
  undefined8 local_1c0;
  undefined8 local_1b8;
  undefined8 local_1b0;
  undefined8 local_1a8;
  undefined8 local_1a0;
  undefined8 local_198;
  undefined8 local_190;
  undefined8 local_188;
  undefined8 local_180;
  undefined8 local_178;
  undefined8 local_170;
  undefined8 uStack_168;
  undefined8 local_160;
  undefined8 uStack_158;
  undefined8 local_150;
  undefined8 uStack_148;
  undefined8 local_140;
  undefined8 local_138;
  undefined8 local_130;
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
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 local_78;
  undefined8 local_70;
  
  FUN_ffff0000b804f1b0();
  lVar7 = FUN_ffff0000b804a88c(param_1);
  if (lVar7 == 0) {
    FUN_ffff0000b804c33c(s_partition_publish__unable_to_ope_ffff0000b808a229);
    return 0xffffffff;
  }
  lVar8 = FUN_ffff0000b804bd14(0x40,*(undefined8 *)(lVar7 + 0x28));
  if (lVar8 == 0) {
    return 0xfffffffb;
  }
  uVar5 = FUN_ffff0000b804aa30(lVar7,lVar8,param_2,0x200);
  if ((int)uVar5 < 0) {
    uVar19 = 0;
    goto LAB_ffff0000b804ebc0;
  }
  if ((*(char *)(lVar8 + 0x1fe) == 'U') && (*(char *)(lVar8 + 0x1ff) == -0x56)) {
    local_78 = 0;
    local_70 = 0;
    local_88 = 0;
    local_80 = 0;
    local_98 = 0;
    local_90 = 0;
    local_a8 = 0;
    local_a0 = 0;
    FUN_ffff0000b804cd40(&local_a8,lVar8 + 0x1be,0x40);
    if ((local_a8._4_1_ == '\0') || (((byte)local_a8 | 0x80) != 0x80)) {
LAB_ffff0000b804eaf4:
      uVar19 = 0;
      uVar12 = local_90;
joined_r0xffff0000b804ec5c:
      if ((local_98._4_1_ != '\0') && (((byte)local_98 | 0x80) == 0x80)) {
        local_90._0_4_ = (uint)uVar12;
        if (((uint)local_90 < *(uint *)(lVar7 + 0x38)) &&
           (local_90._4_4_ = (int)(uVar12 >> 0x20),
           local_90._4_4_ + (uint)local_90 <= *(uint *)(lVar7 + 0x38))) {
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
          local_90 = uVar12;
          if (local_98._4_1_ == -0x12) goto LAB_ffff0000b804ed54;
          FUN_ffff0000b804d6a4(&local_128,s__sp_d_ffff0000b808a253,param_1,1);
          uVar5 = FUN_ffff0000b804b480(param_1,&local_128,local_90 & 0xffffffff,local_90._4_4_);
          uVar19 = uVar19 - ((int)~uVar5 >> 0x1f);
          uVar12 = local_90;
        }
      }
      local_90 = uVar12;
      if ((local_88._4_1_ != '\0') && (((byte)local_88 | 0x80) == 0x80)) {
        if (((uint)local_80 < *(uint *)(lVar7 + 0x38)) &&
           (local_80._4_4_ + (uint)local_80 <= *(uint *)(lVar7 + 0x38))) {
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
          if (local_88._4_1_ == -0x12) goto LAB_ffff0000b804ed54;
          FUN_ffff0000b804d6a4(&local_128,s__sp_d_ffff0000b808a253,param_1,2);
          uVar5 = FUN_ffff0000b804b480(param_1,&local_128,local_80 & 0xffffffff,local_80._4_4_);
          uVar19 = uVar19 - ((int)~uVar5 >> 0x1f);
        }
      }
      if ((local_78._4_1_ != '\0') && (((byte)local_78 | 0x80) == 0x80)) {
        if (((uint)local_70 < *(uint *)(lVar7 + 0x38)) &&
           (local_70._4_4_ + (uint)local_70 <= *(uint *)(lVar7 + 0x38))) {
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
          if (local_78._4_1_ == -0x12) goto LAB_ffff0000b804ed54;
          FUN_ffff0000b804d6a4(&local_128,s__sp_d_ffff0000b808a253,param_1,3);
          uVar5 = FUN_ffff0000b804b480(param_1,&local_128,local_70 & 0xffffffff,local_70._4_4_);
          uVar19 = uVar19 - ((int)~uVar5 >> 0x1f);
        }
      }
    }
    else {
      if ((*(uint *)(lVar7 + 0x38) <= (uint)local_a0) ||
         (*(uint *)(lVar7 + 0x38) < local_a0._4_4_ + (uint)local_a0)) goto LAB_ffff0000b804eaf4;
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
      if (local_a8._4_1_ != -0x12) {
        FUN_ffff0000b804d6a4(&local_128,s__sp_d_ffff0000b808a253,param_1,0);
        uVar5 = FUN_ffff0000b804b480(param_1,&local_128,local_a0 & 0xffffffff,local_a0._4_4_);
        uVar19 = ~uVar5 >> 0x1f;
        uVar12 = local_90;
        goto joined_r0xffff0000b804ec5c;
      }
      uVar19 = 0;
LAB_ffff0000b804ed54:
      uStack_b0 = 0;
      local_b8 = 0;
      uStack_c0 = 0;
      local_c8 = 0;
      uStack_d0 = 0;
      local_d8 = 0;
      uStack_e0 = 0;
      local_e8 = 0;
      uStack_f0 = 0;
      local_f8 = 0;
      uStack_100 = 0;
      local_108 = 0;
      uStack_110 = 0;
      local_118 = 0;
      uStack_120 = 0;
      local_128 = 0;
      uVar5 = FUN_ffff0000b804aa30(lVar7,lVar8,*(long *)(lVar7 + 0x28) + param_2);
      if ((int)uVar5 < 0) goto LAB_ffff0000b804ebc0;
      local_138 = 0;
      local_130 = 0;
      local_140 = 0;
      iVar6 = FUN_ffff0000b804f26c(lVar7,lVar8,&local_140);
      if (iVar6 == 0) {
LAB_ffff0000b804edd0:
        uVar12 = *(ulong *)(lVar7 + 0x28);
        uVar14 = (uint)(uVar12 >> 7);
        uVar1 = (uVar14 + (int)local_130) - 1 & -uVar14;
        if (uVar1 < uVar14) {
          uVar5 = 0;
        }
        else {
          uVar4 = 0;
          if (uVar14 != 0) {
            uVar4 = uVar1 / uVar14;
          }
          uVar16 = 0;
          lVar13 = *(long *)(lVar8 + 0x48);
          iVar6 = (int)local_138;
          do {
            uVar5 = FUN_ffff0000b804aa30
                              (lVar7,lVar8,
                               param_2 + *(long *)(lVar7 + 0x28) * (lVar13 + (ulong)uVar16));
            if ((int)uVar5 < 0) {
              pcVar11 = s_GPT__mmc_read_card_failed_readin_ffff0000b808a259;
              goto LAB_ffff0000b804f1a4;
            }
            uVar17 = uVar16;
            if (uVar14 != 0) {
              local_150 = 0;
              uStack_148 = 0;
              local_160 = 0;
              uStack_158 = 0;
              local_170 = 0;
              uStack_168 = 0;
              local_180 = 0;
              local_178 = 0;
              local_190 = 0;
              local_188 = 0;
              local_1a0 = 0;
              local_198 = 0;
              local_1b0 = 0;
              local_1a8 = 0;
              local_1c0 = 0;
              local_1b8 = 0;
              local_1d0 = 0;
              local_1c8 = 0;
              local_1e0 = 0;
              local_1d8 = 0;
              local_1f0 = 0;
              uStack_1e8 = 0;
              uStack_1f8 = 0;
              local_208 = 0;
              uStack_200 = 0;
              local_1fb = 0;
              local_218 = 0;
              uStack_210 = 0;
              FUN_ffff0000b804cd40(&local_150,lVar8,0x10);
              uVar17 = uVar1;
              if ((char)local_150 != '\0' || local_150._1_1_ != '\0') {
                uVar18 = uVar12 >> 7 & 0xffffffff;
                lVar9 = lVar8;
                iVar15 = 0;
                do {
                  iVar2 = *(int *)(lVar8 + (ulong)(iVar15 + 0x28));
                  iVar3 = *(int *)(lVar8 + (ulong)(iVar15 + 0x20));
                  FUN_ffff0000b804d070(&local_1e0,0,0x48);
                  FUN_ffff0000b804cd40(&local_1e0,lVar8 + (ulong)(iVar15 + 0x38),0x48);
                  uVar10 = local_178;
                  local_198 = CONCAT17(local_1d8._6_1_,
                                       CONCAT16(local_1d8._4_1_,
                                                CONCAT15(local_1d8._2_1_,
                                                         CONCAT14((undefined1)local_1d8,
                                                                  CONCAT13(local_1e0._6_1_,
                                                                           CONCAT12(local_1e0._4_1_,
                                                                                    CONCAT11(
                                                  local_1e0._2_1_,(char)local_1e0)))))));
                  local_190 = CONCAT17(local_1c8._6_1_,
                                       CONCAT16(local_1c8._4_1_,
                                                CONCAT15(local_1c8._2_1_,
                                                         CONCAT14((undefined1)local_1c8,
                                                                  CONCAT13(local_1d0._6_1_,
                                                                           CONCAT12(local_1d0._4_1_,
                                                                                    CONCAT11(
                                                  local_1d0._2_1_,(char)local_1d0)))))));
                  local_188 = CONCAT17(local_1b8._6_1_,
                                       CONCAT16(local_1b8._4_1_,
                                                CONCAT15(local_1b8._2_1_,
                                                         CONCAT14((undefined1)local_1b8,
                                                                  CONCAT13(local_1c0._6_1_,
                                                                           CONCAT12(local_1c0._4_1_,
                                                                                    CONCAT11(
                                                  local_1c0._2_1_,(char)local_1c0)))))));
                  local_180 = CONCAT17(local_1a8._6_1_,
                                       CONCAT16(local_1a8._4_1_,
                                                CONCAT15(local_1a8._2_1_,
                                                         CONCAT14((undefined1)local_1a8,
                                                                  CONCAT13(local_1b0._6_1_,
                                                                           CONCAT12(local_1b0._4_1_,
                                                                                    CONCAT11(
                                                  local_1b0._2_1_,(char)local_1b0)))))));
                  local_178._4_4_ = SUB84(uVar10,4);
                  local_178._0_4_ =
                       CONCAT13(local_1a0._6_1_,
                                CONCAT12(local_1a0._4_1_,
                                         CONCAT11(local_1a0._2_1_,(undefined1)local_1a0)));
                  FUN_ffff0000b804cd40(&local_1f0,lVar9 + 0x10,0x10);
                  FUN_ffff0000b805a9ec(&local_218,&local_1f0);
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
                  FUN_ffff0000b804d6a4(&local_128,s__sp_d_ffff0000b808a253,param_1,uVar19 + 1);
                  uVar5 = FUN_ffff0000b804b480(param_1,&local_128,iVar3,(iVar2 - iVar3) + 1);
                  if (-1 < (int)uVar5) {
                    lVar9 = FUN_ffff0000b804a88c(&local_128);
                    uVar10 = FUN_ffff0000b804d270(&local_218);
                    *(undefined8 *)(lVar9 + 0x48) = uVar10;
                    uVar10 = FUN_ffff0000b804d270(&local_198);
                    *(undefined8 *)(lVar9 + 0x40) = uVar10;
                    *(undefined1 *)(lVar9 + 0x50) = 1;
                    uVar19 = uVar19 + 1;
                  }
                  uVar18 = uVar18 - 1;
                  uVar17 = uVar16;
                  if (uVar18 == 0) break;
                  local_150 = 0;
                  uStack_148 = 0;
                  local_160 = 0;
                  uStack_158 = 0;
                  lVar9 = lVar8 + (ulong)(uint)(iVar6 + iVar15);
                  local_170 = 0;
                  uStack_168 = 0;
                  local_180 = 0;
                  local_178 = 0;
                  local_190 = 0;
                  local_188 = 0;
                  local_1a0 = 0;
                  local_198 = 0;
                  local_1b0 = 0;
                  local_1a8 = 0;
                  local_1c0 = 0;
                  local_1b8 = 0;
                  local_1d0 = 0;
                  local_1c8 = 0;
                  local_1e0 = 0;
                  local_1d8 = 0;
                  local_1f0 = 0;
                  uStack_1e8 = 0;
                  uStack_1f8 = 0;
                  local_208 = 0;
                  uStack_200 = 0;
                  local_1fb = 0;
                  local_218 = 0;
                  uStack_210 = 0;
                  FUN_ffff0000b804cd40(&local_150,lVar9,0x10);
                  uVar17 = uVar1;
                  iVar15 = iVar15 + iVar6;
                } while ((char)local_150 != '\0' || local_150._1_1_ != '\0');
              }
            }
            uVar16 = uVar17 + 1;
          } while (uVar16 < uVar4);
        }
      }
      else {
        uVar5 = FUN_ffff0000b804aa30
                          (lVar7,lVar8,*(long *)(lVar7 + 0x28) * (ulong)(*(int *)(lVar7 + 0x38) - 1)
                          );
        if ((int)uVar5 < 0) {
          pcVar11 = s_GPT__Could_not_read_backup_gpt_f_ffff0000b808e4a0;
        }
        else {
          uVar5 = FUN_ffff0000b804f26c(lVar7,lVar8,&local_140);
          if (uVar5 == 0) goto LAB_ffff0000b804edd0;
          pcVar11 = s_GPT__Primary_and_backup_signatur_ffff0000b809ceaf;
        }
LAB_ffff0000b804f1a4:
        FUN_ffff0000b804c33c(pcVar11);
      }
    }
  }
  else {
    uVar19 = 0;
  }
  FUN_ffff0000b804a920(lVar7);
LAB_ffff0000b804ebc0:
  thunk_FUN_ffff0000b8082558(lVar8);
  if (-1 < (int)uVar5) {
    uVar5 = uVar19;
  }
  return uVar5;
}


