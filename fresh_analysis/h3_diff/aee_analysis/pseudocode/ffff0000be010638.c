
void FUN_ffff0000be010638(long param_1,long *param_2)

{
  undefined4 *puVar1;
  undefined8 *puVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  uint uVar6;
  uint uVar7;
  int iVar8;
  long lVar9;
  long *plVar10;
  long lVar11;
  long lVar12;
  long lVar13;
  ulong uVar14;
  ulong uVar15;
  short sVar16;
  long lVar17;
  undefined8 local_318;
  undefined8 local_310;
  undefined8 local_308;
  undefined8 local_300;
  undefined8 uStack_2f8;
  undefined8 local_2f0;
  undefined8 uStack_2e8;
  undefined8 local_2e0;
  undefined8 uStack_2d8;
  undefined8 local_2d0;
  undefined8 uStack_2c8;
  undefined8 local_2c0;
  undefined8 uStack_2b8;
  undefined8 local_2b0;
  undefined8 uStack_2a8;
  undefined8 local_2a0;
  undefined8 uStack_298;
  uint local_290;
  undefined4 uStack_28c;
  undefined4 uStack_288;
  undefined4 uStack_284;
  undefined8 local_280;
  undefined8 local_278;
  undefined8 local_270;
  undefined8 local_268;
  undefined8 local_260;
  undefined8 local_258;
  undefined8 local_250;
  undefined8 local_248;
  undefined8 local_240;
  undefined8 uStack_238;
  undefined8 local_230;
  undefined8 uStack_228;
  undefined8 local_220;
  undefined8 uStack_218;
  undefined8 local_210;
  undefined8 uStack_208;
  undefined8 local_200;
  undefined8 uStack_1f8;
  undefined8 local_90;
  uint local_88;
  uint uStack_84;
  undefined4 uStack_80;
  undefined4 uStack_7c;
  uint local_78;
  undefined4 uStack_74;
  undefined4 uStack_70;
  
  local_88 = (uint)param_1;
  uVar7 = local_88;
  uStack_84 = (uint)((ulong)param_1 >> 0x20);
  uVar3 = uStack_84;
  if (*(int *)(param_1 + 0x14) == 0) {
    FUN_ffff0000be0114f0(s_32b_kernel_detected_ffff0000be0a69b8);
    lVar13 = *param_2;
    FUN_ffff0000be060ae0(lVar13 + 0x34,0,0x20);
    sVar16 = 1;
    *(undefined4 *)(lVar13 + 0x34) = 4;
    lVar12 = param_2[6];
    if (lVar12 == 0) {
      lVar12 = lVar13 + 0x54;
      lVar17 = 0x54;
    }
    else {
      iVar5 = 0x3000;
      lVar17 = 0x54;
      sVar16 = 1;
      plVar10 = param_2 + 0xc;
      do {
        puVar1 = (undefined4 *)(lVar13 + lVar17);
        sVar16 = sVar16 + 1;
        lVar17 = lVar17 + 0x20;
        *(undefined8 *)(puVar1 + 6) = 7;
        *puVar1 = 1;
        puVar1[1] = iVar5;
        puVar1[2] = 0;
        puVar1[3] = (int)lVar12;
        iVar8 = (int)plVar10[-5];
        puVar1[4] = iVar8;
        puVar1[5] = iVar8;
        iVar5 = iVar5 + iVar8;
        lVar12 = *plVar10;
        plVar10 = plVar10 + 6;
      } while (lVar12 != 0);
      lVar12 = lVar13 + lVar17;
    }
    FUN_ffff0000be0607b0(lVar13,&DAT_ffff0000be0ac557,4);
    *(undefined4 *)(lVar13 + 4) = 0x10101;
    FUN_ffff0000be060ae0(lVar13 + 8,0,8);
    *(undefined8 *)(lVar13 + 0x20) = 0;
    *(undefined8 *)(lVar13 + 0x10) = 0x100280004;
    *(undefined8 *)(lVar13 + 0x18) = 0x3400000000;
    *(undefined4 *)(lVar13 + 0x28) = 0x200034;
    *(short *)(lVar13 + 0x2c) = sVar16;
    *(undefined4 *)(lVar13 + 0x2e) = 0;
    *(undefined2 *)(lVar13 + 0x32) = 0;
    *(int *)(lVar13 + 0x38) = (int)lVar17;
    local_2a0 = 0;
    uStack_298 = 0;
    local_2b0 = 0;
    uStack_2a8 = 0;
    local_2c0 = 0;
    uStack_2b8 = 0;
    local_2d0 = 0;
    uStack_2c8 = 0;
    local_2e0 = 0;
    uStack_2d8 = 0;
    local_2f0 = 0;
    uStack_2e8 = 0;
    local_300 = 0;
    uStack_2f8 = 0;
    local_310 = 0;
    local_308 = 0;
    local_318 = 0;
    FUN_ffff0000be060ae0(&local_318,0,0x88);
    sVar16 = *(short *)(param_1 + 0x17c) + 1;
    local_318._2_6_ = (uint6)((ulong)local_318 >> 0x10) & 0xffffffffff00;
    local_318 = CONCAT62(local_318._2_6_,0x5200);
    local_308._0_4_ = CONCAT22(sVar16,sVar16);
    FUN_ffff0000be060df0((long)&uStack_2f8 + 4,s_vmlinux_ffff0000be0ab2d6,0x10);
    FUN_ffff0000be060df0((long)&uStack_2e8 + 4,s_vmlinux_ffff0000be0ab2d6,0x50);
    uVar4 = FUN_ffff0000be060e5c(&DAT_ffff0000be0bcd54);
    *(uint *)(lVar13 + 0x44) = *(int *)(lVar13 + 0x44) + (uVar4 & 0xfffffffc) + 0x98;
    iVar5 = FUN_ffff0000be060e5c(&DAT_ffff0000be0bcd54);
    local_290 = iVar5 + 1;
    uStack_28c = 0x88;
    uStack_288 = 3;
    FUN_ffff0000be0607b0(lVar12,&local_290,0xc);
    FUN_ffff0000be0607b0(lVar12 + 0xc,&DAT_ffff0000be0bcd54,local_290);
    uVar14 = lVar12 + 0xc + (ulong)local_290 + 3 & 0xfffffffffffffffc;
    FUN_ffff0000be0607b0(uVar14,&local_318,0x88);
    local_240 = 0;
    uStack_238 = 0;
    local_250 = 0;
    local_248 = 0;
    local_260 = 0;
    local_258 = 0;
    local_270 = 0;
    local_268 = 0;
    local_280 = 0;
    local_278 = 0;
    local_290 = 0;
    uStack_28c = 0;
    uStack_288 = 0;
    uStack_284 = 0;
    FUN_ffff0000be060ae0(&local_290,0,0x60);
    local_278 = *(undefined8 *)(param_1 + 0x10);
    local_270 = *(undefined8 *)(param_1 + 0x18);
    local_240 = *(undefined8 *)(param_1 + 0x90);
    uStack_28c = *(undefined4 *)(param_1 + 8);
    uStack_288 = (undefined4)*(undefined8 *)(param_1 + 0x88);
    uStack_284 = (undefined4)((ulong)*(undefined8 *)(param_1 + 0x88) >> 0x20);
    local_260 = *(undefined8 *)(param_1 + 0x78);
    local_258 = *(undefined8 *)(param_1 + 0x80);
    local_290 = 0;
    local_268 = *(undefined8 *)(param_1 + 0x20);
    local_250 = *(undefined8 *)(param_1 + 0x68);
    local_248 = *(undefined8 *)(param_1 + 0x70);
    uVar4 = FUN_ffff0000be060e5c(s_MRDUMP01_ffff0000be0b97ec);
    *(uint *)(lVar13 + 0x44) = *(int *)(lVar13 + 0x44) + (uVar4 & 0xfffffffc) + 0x70;
    iVar5 = FUN_ffff0000be060e5c(s_MRDUMP01_ffff0000be0b97ec);
    local_88 = iVar5 + 1;
    uStack_84 = 0x60;
    uStack_80 = 0xaee0;
    FUN_ffff0000be0607b0(uVar14 + 0x88,&local_88,0xc);
    FUN_ffff0000be0607b0(uVar14 + 0x94,s_MRDUMP01_ffff0000be0b97ec,local_88);
    uVar14 = uVar14 + 0x94 + (ulong)local_88 + 3 & 0xfffffffffffffffc;
    FUN_ffff0000be0607b0(uVar14,&local_290,0x60);
    uVar14 = uVar14 + 0x60;
    if (*(int *)(param_1 + 8) != 0) {
      uVar4 = 0;
      do {
        local_200 = 0;
        uStack_1f8 = 0;
        local_210 = 0;
        uStack_208 = 0;
        local_220 = 0;
        uStack_218 = 0;
        local_230 = 0;
        uStack_228 = 0;
        local_240 = 0;
        uStack_238 = 0;
        local_250 = 0;
        local_248 = 0;
        local_260 = 0;
        local_258 = 0;
        local_270 = 0;
        local_268 = 0;
        local_280 = 0;
        local_278 = 0;
        local_290 = 0;
        uStack_28c = 0;
        uStack_288 = 0;
        uStack_284 = 0;
        local_88 = 0;
        uStack_84 = 0;
        uStack_80 = 0;
        uStack_7c = 0;
        FUN_ffff0000be060ae0(&local_290,0,0xa0);
        iVar5 = FUN_ffff0000be0611dc(&local_88,0x10,s_CPU_d_ffff0000be0b4f74,uVar4);
        if (iVar5 < 1) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000be05ee98
                    (s_app_aee_mrdump_elf_c_ffff0000be0bbb56,0x74,s_err_>_0_ffff0000be0bbb4e);
        }
        lVar12 = (long)(int)uVar4;
        uVar4 = uVar4 + 1;
        local_270 = CONCAT44(local_270._4_4_,uVar4);
        FUN_ffff0000be0607b0(&local_240,param_1 + 0x180 + lVar12 * 0x180,0x48);
        uVar6 = FUN_ffff0000be060e5c(&local_88);
        *(uint *)(lVar13 + 0x44) = *(int *)(lVar13 + 0x44) + (uVar6 & 0xfffffffc) + 0xb0;
        iVar5 = FUN_ffff0000be060e5c(&local_88);
        uStack_74 = 0xa0;
        uStack_70 = 1;
        local_78 = iVar5 + 1;
        FUN_ffff0000be0607b0(uVar14,&local_78,0xc);
        FUN_ffff0000be0607b0(uVar14 + 0xc,&local_88,local_78);
        uVar14 = uVar14 + 0xc + (ulong)local_78 + 3 & 0xfffffffffffffffc;
        FUN_ffff0000be0607b0(uVar14,&local_290,0xa0);
        uVar14 = uVar14 + 0xa0;
      } while (uVar4 < *(uint *)(param_1 + 8));
    }
    local_88 = uVar7;
    uStack_84 = uVar3;
    uVar7 = FUN_ffff0000be060e5c(s_MRDUMP11_ffff0000be0a7a51);
    *(uint *)(lVar13 + 0x44) = *(int *)(lVar13 + 0x44) + (uVar7 & 0xfffffffc) + 0x18;
  }
  else {
    FUN_ffff0000be0114f0(s_64b_kernel_detected_ffff0000be0b4f5f);
    lVar13 = *param_2;
    FUN_ffff0000be060ae0(lVar13 + 0x40,0,0x38);
    *(undefined4 *)(lVar13 + 0x40) = 4;
    lVar12 = param_2[6];
    if (lVar12 == 0) {
      lVar12 = lVar13 + 0x78;
      sVar16 = 1;
      lVar17 = 0x78;
    }
    else {
      lVar17 = 0x78;
      lVar11 = 0x3000;
      sVar16 = 1;
      plVar10 = param_2 + 0xc;
      do {
        puVar2 = (undefined8 *)(lVar13 + lVar17);
        lVar17 = lVar17 + 0x38;
        sVar16 = sVar16 + 1;
        *puVar2 = 0x700000001;
        puVar2[1] = lVar11;
        puVar2[2] = 0;
        puVar2[3] = lVar12;
        lVar12 = plVar10[-5];
        puVar2[6] = 0;
        puVar2[4] = lVar12;
        puVar2[5] = lVar12;
        lVar11 = lVar12 + lVar11;
        lVar12 = *plVar10;
        plVar10 = plVar10 + 6;
      } while (lVar12 != 0);
      lVar12 = lVar13 + lVar17;
    }
    FUN_ffff0000be0607b0(lVar13,&DAT_ffff0000be0ac557,4);
    *(undefined4 *)(lVar13 + 4) = 0x10102;
    FUN_ffff0000be060ae0(lVar13 + 8,0,8);
    *(undefined8 *)(lVar13 + 0x20) = 0x40;
    *(undefined8 *)(lVar13 + 0x28) = 0;
    *(short *)(lVar13 + 0x38) = sVar16;
    *(undefined8 *)(lVar13 + 0x10) = 0x100b70004;
    *(undefined8 *)(lVar13 + 0x18) = 0;
    *(undefined4 *)(lVar13 + 0x3a) = 0;
    *(undefined2 *)(lVar13 + 0x3e) = 0;
    *(long *)(lVar13 + 0x48) = lVar17;
    *(undefined8 *)(lVar13 + 0x30) = 0x38004000000000;
    local_2a0 = 0;
    uStack_298 = 0;
    local_2b0 = 0;
    uStack_2a8 = 0;
    local_2c0 = 0;
    uStack_2b8 = 0;
    local_2d0 = 0;
    uStack_2c8 = 0;
    local_2e0 = 0;
    uStack_2d8 = 0;
    local_2f0 = 0;
    uStack_2e8 = 0;
    local_300 = 0;
    uStack_2f8 = 0;
    local_310 = 0;
    local_308 = 0;
    local_318 = 0;
    FUN_ffff0000be060ae0(&local_318,0,0x88);
    iVar5 = *(int *)(param_1 + 0x17c) + 1;
    local_318._2_6_ = (uint6)((ulong)local_318 >> 0x10) & 0xffffffffff00;
    local_318 = CONCAT62(local_318._2_6_,0x5200);
    local_308 = CONCAT44(iVar5,iVar5);
    FUN_ffff0000be060df0(&local_2f0,s_vmlinux_ffff0000be0ab2d6,0x10);
    FUN_ffff0000be060df0(&local_2e0,s_vmlinux_ffff0000be0ab2d6,0x50);
    uVar14 = FUN_ffff0000be060e5c(&DAT_ffff0000be0bcd54);
    *(long *)(lVar13 + 0x60) =
         *(long *)(lVar13 + 0x60) + ((long)(((uVar14 >> 2) << 0x22) + 0x9800000000) >> 0x20);
    iVar5 = FUN_ffff0000be060e5c(&DAT_ffff0000be0bcd54);
    local_290 = iVar5 + 1;
    uStack_28c = 0x88;
    uStack_288 = 3;
    FUN_ffff0000be0607b0(lVar12,&local_290,0xc);
    FUN_ffff0000be0607b0(lVar12 + 0xc,&DAT_ffff0000be0bcd54,local_290);
    uVar15 = lVar12 + 0xc + (ulong)local_290 + 3 & 0xfffffffffffffffc;
    FUN_ffff0000be0607b0(uVar15,&local_318,0x88);
    local_240 = 0;
    uStack_238 = 0;
    local_250 = 0;
    local_248 = 0;
    local_260 = 0;
    local_258 = 0;
    local_270 = 0;
    local_268 = 0;
    local_280 = 0;
    local_278 = 0;
    local_290 = 0;
    uStack_28c = 0;
    uStack_288 = 0;
    uStack_284 = 0;
    FUN_ffff0000be060ae0(&local_290,0,0x60);
    local_278 = *(undefined8 *)(param_1 + 0x10);
    local_270 = *(undefined8 *)(param_1 + 0x18);
    local_240 = *(undefined8 *)(param_1 + 0x90);
    uStack_28c = *(undefined4 *)(param_1 + 8);
    uStack_288 = (undefined4)*(undefined8 *)(param_1 + 0x88);
    uStack_284 = (undefined4)((ulong)*(undefined8 *)(param_1 + 0x88) >> 0x20);
    local_260 = *(undefined8 *)(param_1 + 0x78);
    local_258 = *(undefined8 *)(param_1 + 0x80);
    local_290 = 0;
    local_268 = *(undefined8 *)(param_1 + 0x20);
    local_250 = *(undefined8 *)(param_1 + 0x68);
    local_248 = *(undefined8 *)(param_1 + 0x70);
    uVar14 = FUN_ffff0000be060e5c(s_MRDUMP01_ffff0000be0b97ec);
    *(long *)(lVar13 + 0x60) =
         *(long *)(lVar13 + 0x60) + ((long)(((uVar14 >> 2) << 0x22) + 0x7000000000) >> 0x20);
    iVar5 = FUN_ffff0000be060e5c(s_MRDUMP01_ffff0000be0b97ec);
    local_88 = iVar5 + 1;
    uStack_84 = 0x60;
    uStack_80 = 0xaee0;
    FUN_ffff0000be0607b0(uVar15 + 0x88,&local_88,0xc);
    FUN_ffff0000be0607b0(uVar15 + 0x94,s_MRDUMP01_ffff0000be0b97ec,local_88);
    uVar14 = uVar15 + 0x94 + (ulong)local_88 + 3 & 0xfffffffffffffffc;
    FUN_ffff0000be0607b0(uVar14,&local_290,0x60);
    FUN_ffff0000be060ae0(&local_290,0,0x200);
    lVar12 = *(long *)(param_1 + 0x90);
    lVar17 = *(long *)(param_1 + 0x50);
    FUN_ffff0000be060ae0(&local_290,0,0x200);
    iVar5 = FUN_ffff0000be0611dc
                      (&local_290,0x200,s_PAGESIZE__llu_NUMBER_kimage_voff_ffff0000be0b97f5,
                       *(undefined8 *)(param_1 + 0x60),*(undefined8 *)(param_1 + 0x50),
                       *(undefined8 *)(param_1 + 0x48),0x40 - *(long *)(param_1 + 0x18),
                       *(undefined8 *)(param_1 + 0x88),*(long *)(param_1 + 0x18),
                       *(undefined8 *)(param_1 + 0x28),*(undefined8 *)(param_1 + 0x58),
                       lVar17 + lVar12);
    if (iVar5 < 1) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98(s_app_aee_mrdump_elf_c_ffff0000be0bbb56,0x1b6,s_err_>_0_ffff0000be0bbb4e)
      ;
    }
    uVar4 = FUN_ffff0000be060e5c(&local_290);
    uVar6 = FUN_ffff0000be060e5c(s_VMCOREINFO_ffff0000be0c5fc3);
    *(long *)(lVar13 + 0x60) =
         *(long *)(lVar13 + 0x60) +
         (long)(int)((uVar6 & 0xfffffffc) + (uVar4 + 3 & 0xfffffffc) + 0x10);
    iVar5 = FUN_ffff0000be060e5c(s_VMCOREINFO_ffff0000be0c5fc3);
    local_88 = iVar5 + 1;
    uStack_80 = 0;
    uStack_84 = uVar4;
    FUN_ffff0000be0607b0(uVar14 + 0x60,&local_88,0xc);
    FUN_ffff0000be0607b0(uVar14 + 0x6c,s_VMCOREINFO_ffff0000be0c5fc3,local_88);
    uVar14 = uVar14 + 0x6c + (ulong)local_88 + 3 & 0xfffffffffffffffc;
    FUN_ffff0000be0607b0(uVar14,&local_290,(ulong)uVar4);
    uVar14 = uVar14 + uVar4 + 3 & 0xfffffffffffffffc;
    lVar12 = FUN_ffff0000be05f1bc(0x1000);
    if (lVar12 == 0) {
      FUN_ffff0000be05fd98
                (s__s__malloc_fail_ffff0000be0b8595,
                 s_mrdump_core64_write_mblock_reser_ffff0000be0b7416);
      iVar5 = *(int *)(param_1 + 8);
    }
    else {
      lVar17 = FUN_ffff0000be05f1bc(0x200);
      if (lVar17 == 0) {
        FUN_ffff0000be05fd98
                  (s__s__malloc_fail_ffff0000be0b8595,
                   s_mrdump_core64_write_mblock_reser_ffff0000be0b7416);
      }
      else {
        local_220 = 0;
        uStack_218 = 0;
        local_230 = 0;
        uStack_228 = 0;
        local_240 = 0;
        uStack_238 = 0;
        local_250 = 0;
        local_248 = 0;
        local_260 = 0;
        local_258 = 0;
        local_270 = 0;
        local_268 = 0;
        local_280 = 0;
        local_278 = 0;
        local_290 = 0;
        uStack_28c = 0;
        uStack_288 = 0;
        uStack_284 = 0;
        local_78 = 0;
        uStack_74 = 0;
        local_90 = 0;
        FUN_ffff0000be060ae0(lVar12,0,0x1000);
        FUN_ffff0000be060ae0(lVar17,0,0x200);
        FUN_ffff0000be060e7c(lVar12,&DAT_ffff0000be0a3157,1);
        iVar5 = 0;
        do {
          iVar8 = FUN_ffff0000be06534c(iVar5,&local_290,&local_78,&local_90);
          if (iVar8 == 0) {
            FUN_ffff0000be060ae0(lVar17,0,0x200);
            FUN_ffff0000be0611dc
                      (lVar17,0x200,s___name____s____start___0x_llx_____ffff0000be0b3d88,&local_290,
                       CONCAT44(uStack_74,local_78),local_90);
            lVar11 = FUN_ffff0000be060e5c(lVar12);
            lVar9 = FUN_ffff0000be060e5c(lVar17);
            if (0xfdb < (ulong)(lVar9 + lVar11)) {
              FUN_ffff0000be05fd98
                        (s__s__d__mrdump_MBLOCK_RSV_INFO_is_ffff0000be0bcd59,
                         s_mrdump_core64_write_mblock_reser_ffff0000be0b7416,0x17f);
              break;
            }
            FUN_ffff0000be060e7c(lVar12,lVar17,lVar9);
          }
          iVar5 = iVar5 + 1;
        } while (iVar5 != 0x80);
        FUN_ffff0000be060e7c(lVar12,s___name_______start_______size____ffff0000be0a69cd,0x24);
        uVar4 = FUN_ffff0000be060e5c(lVar12);
        uVar6 = FUN_ffff0000be060e5c(s_MBLOCK_RSV_INFO_ffff0000be0bab40);
        *(long *)(lVar13 + 0x60) =
             *(long *)(lVar13 + 0x60) +
             (long)(int)((uVar6 & 0xfffffffc) + (uVar4 + 3 & 0xfffffffc) + 0x10);
        iVar5 = FUN_ffff0000be060e5c(s_MBLOCK_RSV_INFO_ffff0000be0bab40);
        local_88 = iVar5 + 1;
        uStack_80 = 0;
        uStack_84 = uVar4;
        FUN_ffff0000be0607b0(uVar14,&local_88,0xc);
        FUN_ffff0000be0607b0(uVar14 + 0xc,s_MBLOCK_RSV_INFO_ffff0000be0bab40,local_88);
        uVar14 = uVar14 + 0xc + (ulong)local_88 + 3 & 0xfffffffffffffffc;
        FUN_ffff0000be0607b0(uVar14,lVar12,(ulong)uVar4);
        uVar14 = uVar14 + uVar4 + 3 & 0xfffffffffffffffc;
        thunk_FUN_ffff0000be09937c(lVar17);
      }
      thunk_FUN_ffff0000be09937c(lVar12);
      iVar5 = *(int *)(param_1 + 8);
    }
    if (iVar5 != 0) {
      uVar4 = 0;
      do {
        FUN_ffff0000be060ae0(&local_290,0,0x188);
        local_88 = 0;
        uStack_84 = 0;
        uStack_80 = 0;
        uStack_7c = 0;
        FUN_ffff0000be060ae0(&local_290,0,0x188);
        iVar5 = FUN_ffff0000be0611dc(&local_88,0x10,s_CPU_d_ffff0000be0b4f74,uVar4);
        if (iVar5 < 1) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000be05ee98
                    (s_app_aee_mrdump_elf_c_ffff0000be0bbb56,0x129,s_err_>_0_ffff0000be0bbb4e);
        }
        lVar12 = (long)(int)uVar4;
        uVar4 = uVar4 + 1;
        local_270 = CONCAT44(local_270._4_4_,uVar4);
        FUN_ffff0000be0607b0(&local_220,param_1 + 0x180 + lVar12 * 0x180,0x110);
        uVar15 = FUN_ffff0000be060e5c(&local_88);
        *(long *)(lVar13 + 0x60) =
             *(long *)(lVar13 + 0x60) + ((long)(((uVar15 >> 2) << 0x22) + 0x19800000000) >> 0x20);
        iVar5 = FUN_ffff0000be060e5c(&local_88);
        uStack_74 = 0x188;
        uStack_70 = 1;
        local_78 = iVar5 + 1;
        FUN_ffff0000be0607b0(uVar14,&local_78,0xc);
        FUN_ffff0000be0607b0(uVar14 + 0xc,&local_88,local_78);
        uVar14 = uVar14 + 0xc + (ulong)local_78 + 3 & 0xfffffffffffffffc;
        FUN_ffff0000be0607b0(uVar14,&local_290,0x188);
        uVar14 = uVar14 + 0x188;
      } while (uVar4 < *(uint *)(param_1 + 8));
    }
    local_88 = uVar7;
    uStack_84 = uVar3;
    uVar15 = FUN_ffff0000be060e5c(s_MRDUMP11_ffff0000be0a7a51);
    *(long *)(lVar13 + 0x60) =
         *(long *)(lVar13 + 0x60) + ((long)(((uVar15 >> 2) << 0x22) + 0x1800000000) >> 0x20);
  }
  iVar5 = FUN_ffff0000be060e5c(s_MRDUMP11_ffff0000be0a7a51);
  local_290 = iVar5 + 1;
  uStack_28c = 8;
  uStack_288 = 0xbeef;
  FUN_ffff0000be0607b0(uVar14,&local_290,0xc);
  FUN_ffff0000be0607b0(uVar14 + 0xc,s_MRDUMP11_ffff0000be0a7a51,local_290);
  FUN_ffff0000be0607b0(uVar14 + 0xc + (ulong)local_290 + 3 & 0xfffffffffffffffc,&local_88,8);
  return;
}


