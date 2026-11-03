
undefined8 FUN_ffff0000be00ae40(ulong *param_1)

{
  long lVar1;
  undefined4 *puVar2;
  byte bVar3;
  byte bVar4;
  undefined8 uVar5;
  uint uVar6;
  char cVar7;
  char cVar8;
  undefined1 uVar9;
  char cVar10;
  undefined1 uVar11;
  char cVar12;
  bool bVar13;
  undefined1 uVar14;
  undefined2 uVar16;
  int iVar17;
  undefined4 uVar18;
  uint uVar19;
  uint uVar20;
  undefined8 *puVar21;
  long lVar22;
  undefined8 uVar23;
  undefined8 uVar24;
  ulong uVar25;
  undefined1 uVar26;
  long lVar27;
  code *pcVar28;
  int iVar29;
  ulong uVar30;
  ulong *puVar31;
  ulong uVar32;
  undefined8 uVar33;
  ulong uVar34;
  int iVar35;
  undefined8 uVar36;
  undefined2 uVar15;
  
  uVar32 = param_1[2];
  uVar6 = (uint)param_1[3];
  uVar30 = (ulong)uVar6;
  puVar21 = (undefined8 *)FUN_ffff0000be05f1bc(0x528);
  if (puVar21 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__alloc_central_directory_b_ffff0000be0baac4,s_kzip_close_ffff0000be0af403,
               0x2ae);
    return 0;
  }
  if ((int)uVar6 < 1) {
    uVar25 = param_1[2];
    iVar35 = 0;
  }
  else {
    lVar27 = 0;
    iVar35 = 0;
    lVar1 = (long)puVar21 + 0x2e;
    do {
      uVar25 = param_1[0x388];
      uVar24 = *(undefined8 *)((long)param_1 + lVar27 + 0x30);
      uVar5 = *(undefined8 *)((long)param_1 + lVar27 + 0x38);
      uVar36 = *(undefined8 *)((long)param_1 + lVar27 + 0x20);
      *(undefined4 *)puVar21 = 0x2014b50;
      uVar20 = *(uint *)((long)param_1 + lVar27 + 0x28);
      uVar33 = *(undefined8 *)((long)param_1 + lVar27 + 0x40);
      uVar18 = *(undefined4 *)((long)param_1 + lVar27 + 0x48);
      if ((int)uVar25 == 0) {
        iVar17 = 0;
        uVar14 = 0x2d;
        uVar26 = 0;
      }
      else {
        uVar15 = FUN_ffff0000be05f5e0(&DAT_ffff0000be0ac457,0,0);
        uVar14 = (undefined1)uVar15;
        iVar17 = (int)param_1[0x388];
        uVar26 = (undefined1)((ushort)uVar15 >> 8);
      }
      *(undefined1 *)((long)puVar21 + 5) = uVar26;
      bVar13 = (uVar20 & 0xfffffffe) == 8;
      *(undefined1 *)((long)puVar21 + 7) = uVar26;
      uVar15 = *(undefined2 *)((long)param_1 + lVar27 + 0x4c);
      uVar16 = *(undefined2 *)((long)param_1 + lVar27 + 0x50);
      *(undefined1 *)((long)puVar21 + 4) = uVar14;
      bVar3 = bVar13 << 1 | 4;
      if (uVar20 != 2) {
        bVar3 = bVar13 << 1;
      }
      bVar4 = 0xe;
      if (uVar20 != 1) {
        bVar4 = bVar3 | 8;
      }
      *(undefined1 *)((long)puVar21 + 6) = uVar14;
      *(undefined2 *)((long)puVar21 + 0xc) = uVar15;
      *(undefined2 *)((long)puVar21 + 9) = 0x800;
      *(undefined1 *)((long)puVar21 + 0xb) = 0;
      *(byte *)(puVar21 + 1) = bVar4 | iVar17 != 0;
      *(char *)((long)puVar21 + 0x11) = (char)((uint)uVar18 >> 8);
      *(char *)((long)puVar21 + 0xe) = (char)uVar16;
      *(char *)((long)puVar21 + 0x12) = (char)((uint)uVar18 >> 0x10);
      *(char *)((long)puVar21 + 0xf) = (char)((ushort)uVar16 >> 8);
      *(char *)(puVar21 + 2) = (char)uVar18;
      *(undefined8 *)((long)puVar21 + 0x14) = 0xffffffffffffffff;
      *(char *)((long)puVar21 + 0x13) = (char)((uint)uVar18 >> 0x18);
      uVar15 = 0x444;
      if (iVar17 == 0) {
        uVar15 = 0x1c;
      }
      uVar16 = FUN_ffff0000be060e5c(uVar36);
      *(char *)((long)puVar21 + 0x1e) = (char)uVar15;
      puVar21[4] = 0;
      *(char *)((long)puVar21 + 0x1f) = (char)((ushort)uVar15 >> 8);
      *(char *)((long)puVar21 + 0x1c) = (char)uVar16;
      *(char *)((long)puVar21 + 0x1d) = (char)((ushort)uVar16 >> 8);
      *(undefined4 *)(puVar21 + 5) = 0xffff0000;
      *(undefined2 *)((long)puVar21 + 0x2c) = 0xffff;
      if (iVar17 != 0) {
        FUN_ffff0000be0093a0(param_1);
        FUN_ffff0000be060e5c(uVar36);
        lVar22 = FUN_ffff0000be05f1bc();
        if (lVar22 == 0) {
          FUN_ffff0000be05fd98
                    (s__s__d__No_memory_for_encrypted_f_ffff0000be0ab220,
                     s_put_centralheader_ffff0000be0b8538,0x1e4);
        }
        else {
          uVar23 = FUN_ffff0000be060e5c(uVar36);
          iVar17 = FUN_ffff0000be0091e4
                             (uVar36,lVar22,uVar23,param_1 + 0x38b,param_1 + 0x3c8,param_1 + 0x389,
                              param_1 + 0x40c);
          if (iVar17 == 0) {
            uVar23 = FUN_ffff0000be060e5c(uVar36);
            FUN_ffff0000be0607b0(lVar1,lVar22,uVar23);
            FUN_ffff0000be0093a0(param_1);
            thunk_FUN_ffff0000be09937c(lVar22);
            goto LAB_ffff0000be00b0b0;
          }
          FUN_ffff0000be05fd98
                    (s__s__d__encrypt_fail_ffff0000be0ad296,s_put_centralheader_ffff0000be0b8538,
                     0x1ea);
          thunk_FUN_ffff0000be09937c(lVar22);
        }
LAB_ffff0000be00b7a8:
        FUN_ffff0000be05fd98
                  (s__s__d__Error__write_central_head_ffff0000be0ad26d,s_kzip_close_ffff0000be0af403
                   ,0x2ba);
        goto LAB_ffff0000be00b7c0;
      }
      uVar23 = FUN_ffff0000be060e5c(uVar36);
      FUN_ffff0000be0607b0(lVar1,uVar36,uVar23);
LAB_ffff0000be00b0b0:
      lVar22 = FUN_ffff0000be060e5c(uVar36);
      puVar2 = (undefined4 *)(lVar1 + lVar22);
      uVar25 = param_1[0x388];
      *(char *)((long)puVar2 + 5) = (char)((ulong)uVar33 >> 8);
      *(char *)((long)puVar2 + 6) = (char)((ulong)uVar33 >> 0x10);
      *(char *)((long)puVar2 + 7) = (char)((ulong)uVar33 >> 0x18);
      *(char *)(puVar2 + 2) = (char)((ulong)uVar33 >> 0x20);
      *(char *)((long)puVar2 + 9) = (char)((ulong)uVar33 >> 0x28);
      *(char *)((long)puVar2 + 0xb) = (char)((ulong)uVar33 >> 0x38);
      *(char *)((long)puVar2 + 10) = (char)((ulong)uVar33 >> 0x30);
      *(char *)((long)puVar2 + 0xd) = (char)((ulong)uVar5 >> 8);
      *(char *)(puVar2 + 1) = (char)uVar33;
      *(char *)((long)puVar2 + 0xe) = (char)((ulong)uVar5 >> 0x10);
      *(char *)((long)puVar2 + 0xf) = (char)((ulong)uVar5 >> 0x18);
      *puVar2 = 0x180001;
      *(char *)(puVar2 + 4) = (char)((ulong)uVar5 >> 0x20);
      *(char *)((long)puVar2 + 0x11) = (char)((ulong)uVar5 >> 0x28);
      *(char *)((long)puVar2 + 0x13) = (char)((ulong)uVar5 >> 0x38);
      *(char *)((long)puVar2 + 0x12) = (char)((ulong)uVar5 >> 0x30);
      *(char *)((long)puVar2 + 0x16) = (char)((ulong)uVar24 >> 0x10);
      *(char *)((long)puVar2 + 0x17) = (char)((ulong)uVar24 >> 0x18);
      *(char *)((long)puVar2 + 0x15) = (char)((ulong)uVar24 >> 8);
      *(char *)(puVar2 + 3) = (char)uVar5;
      *(char *)(puVar2 + 5) = (char)uVar24;
      *(char *)(puVar2 + 6) = (char)((ulong)uVar24 >> 0x20);
      *(char *)((long)puVar2 + 0x19) = (char)((ulong)uVar24 >> 0x28);
      *(char *)((long)puVar2 + 0x1b) = (char)((ulong)uVar24 >> 0x38);
      *(char *)((long)puVar2 + 0x1a) = (char)((ulong)uVar24 >> 0x30);
      iVar29 = (int)puVar21;
      iVar17 = (int)puVar2;
      if ((int)uVar25 == 0) {
        uVar20 = (iVar17 + 0x1c) - iVar29;
      }
      else {
        *(undefined2 *)(puVar2 + 8) = 0;
        puVar2[7] = 0x22451;
        uVar18 = FUN_ffff0000be0553a4(0,0,0);
        uVar24 = FUN_ffff0000be060e5c(s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000be0b3b9d);
        uVar18 = FUN_ffff0000be0553a4
                           (uVar18,s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000be0b3b9d,uVar24);
        uVar25 = param_1[0x388];
        *(char *)((long)puVar2 + 0x26) = (char)uVar18;
        *(undefined4 *)((long)puVar2 + 0x22) = 0x42452;
        *(char *)(puVar2 + 10) = (char)((uint)uVar18 >> 0x10);
        *(char *)((long)puVar2 + 0x29) = (char)((uint)uVar18 >> 0x18);
        *(char *)((long)puVar2 + 0x27) = (char)((uint)uVar18 >> 8);
        if ((int)uVar25 == 0) {
          uVar20 = (iVar17 + 0x2a) - iVar29;
        }
        else {
          *(undefined4 *)((long)puVar2 + 0x2a) = 0x62453;
          FUN_ffff0000be0607b0((long)puVar2 + 0x2e,&DAT_ffff0000be0af40e,6);
          if ((int)param_1[0x388] == 0) {
            uVar20 = (iVar17 + 0x34) - iVar29;
          }
          else {
            puVar2[0xd] = 0x1002554;
            lVar22 = FUN_ffff0000be05f1bc(0x100);
            if (lVar22 == 0) {
              FUN_ffff0000be05fd98
                        (s__s__d__alloc_emsg_fail__no_enoug_ffff0000be0b602a,
                         s_put_centralheader_ffff0000be0b8538,0x237);
                    /* WARNING: Subroutine does not return */
              FUN_ffff0000be05ee98
                        (s_app_aee_mrdump_zip_c_ffff0000be0bdf6a,0x238,&DAT_ffff0000be0a553d);
            }
            FUN_ffff0000be060ae0(lVar22,0,0x100);
            FUN_ffff0000be0607b0(puVar2 + 0xe,lVar22,0x100);
            puVar2[0x4e] = 0x1002555;
            FUN_ffff0000be060ae0(lVar22,0,0x100);
            FUN_ffff0000be0607b0(puVar2 + 0x4f,lVar22,0x100);
            thunk_FUN_ffff0000be09937c(lVar22);
            if ((int)param_1[0x388] == 0) {
              uVar20 = (iVar17 + 0x23c) - iVar29;
            }
            else {
              puVar2[0x8f] = 0x1002454;
              lVar22 = FUN_ffff0000be05f1bc(0x100);
              if (lVar22 == 0) {
                FUN_ffff0000be05fd98
                          (s__s__d__alloc_emsg_fail__no_enoug_ffff0000be0b602a,
                           s_put_centralheader_ffff0000be0b8538,0x261);
                    /* WARNING: Subroutine does not return */
                FUN_ffff0000be05ee98
                          (s_app_aee_mrdump_zip_c_ffff0000be0bdf6a,0x262,&DAT_ffff0000be0a553d);
              }
              FUN_ffff0000be060ae0(lVar22,0,0x100);
              FUN_ffff0000be0607b0(lVar22,param_1 + 0x3cc,0x100);
              FUN_ffff0000be0607b0(puVar2 + 0x90,lVar22,0x100);
              puVar2[0xd0] = 0x1002455;
              FUN_ffff0000be060ae0(lVar22,0,0x100);
              FUN_ffff0000be0607b0(lVar22,param_1 + 0x3ec,0x100);
              FUN_ffff0000be0607b0(puVar2 + 0xd1,lVar22,0x100);
              thunk_FUN_ffff0000be09937c(lVar22);
              uVar20 = (iVar17 + 0x444) - iVar29;
            }
          }
        }
      }
      if ((int)uVar20 < 0) goto LAB_ffff0000be00b7a8;
      uVar19 = (*(code *)param_1[0x40e])(param_1[0x40d],puVar21,uVar20);
      if ((int)uVar19 < 1) {
        uVar25 = param_1[2];
      }
      else {
        uVar25 = param_1[2] + (ulong)uVar19;
        param_1[2] = uVar25;
      }
      if (uVar25 - *param_1 >> 0x18 != 0) {
        FUN_ffff0000be011708(uVar25 >> 0x14);
        uVar25 = param_1[2];
        *param_1 = uVar25;
      }
      if (0xffff < uVar25 - param_1[1]) {
        FUN_ffff0000be0357ec();
        uVar25 = param_1[2];
        param_1[1] = uVar25;
      }
      if (uVar19 != uVar20) goto LAB_ffff0000be00b7c0;
      lVar27 = lVar27 + 0x38;
      iVar35 = iVar35 + uVar20;
    } while (uVar30 * 0x38 != lVar27);
  }
  FUN_ffff0000be0114f0
            (s__s__current_size__lld_hoffset__l_ffff0000be0ac433,s_kzip_close_ffff0000be0af403,
             uVar25,uVar32);
  uVar25 = param_1[0x388];
  uVar34 = param_1[2];
  *(undefined4 *)(puVar21 + 1) = 0;
  *puVar21 = 0x2c06064b50;
  if ((int)uVar25 == 0) {
    *(undefined2 *)((long)puVar21 + 0xc) = 0x2d;
  }
  else {
    uVar15 = FUN_ffff0000be05f5e0(&DAT_ffff0000be0ac457,0,0);
    uVar25 = param_1[0x388];
    *(char *)((long)puVar21 + 0xc) = (char)uVar15;
    *(char *)((long)puVar21 + 0xd) = (char)((ushort)uVar15 >> 8);
    if ((int)uVar25 != 0) {
      uVar15 = FUN_ffff0000be05f5e0(&DAT_ffff0000be0ac457,0,0);
      uVar26 = (undefined1)((ushort)uVar15 >> 8);
      *(char *)((long)puVar21 + 0xe) = (char)uVar15;
      goto LAB_ffff0000be00b4d8;
    }
  }
  uVar26 = 0;
  *(undefined1 *)((long)puVar21 + 0xe) = 0x2d;
LAB_ffff0000be00b4d8:
  *(undefined1 *)((long)puVar21 + 0xf) = uVar26;
  uVar11 = (undefined1)(uVar6 >> 8);
  uVar26 = (undefined1)(uVar6 >> 0x10);
  cVar12 = (char)(uVar6 >> 0x18);
  *(undefined1 *)((long)puVar21 + 0x19) = uVar11;
  *(undefined1 *)((long)puVar21 + 0x21) = uVar11;
  cVar7 = cVar12 >> 7;
  *(char *)((long)puVar21 + 0x1c) = cVar7;
  *(char *)((long)puVar21 + 0x24) = cVar7;
  *(undefined1 *)((long)puVar21 + 0x1a) = uVar26;
  *(undefined1 *)((long)puVar21 + 0x22) = uVar26;
  cVar10 = (char)((uint)iVar35 >> 0x18);
  cVar8 = cVar10 >> 7;
  *(char *)((long)puVar21 + 0x2e) = cVar8;
  *(char *)((long)puVar21 + 0x1b) = cVar12;
  *(char *)((long)puVar21 + 0x23) = cVar12;
  *(char *)((long)puVar21 + 0x2c) = cVar8;
  *(char *)((long)puVar21 + 0x2d) = cVar8;
                    /* WARNING: Store size is inaccurate */
  *(int *)((long)puVar21 + 0x2f) = iVar35 >> 0x1f;
  *(char *)((long)puVar21 + 0x31) = (char)(uVar32 >> 8);
  *(char *)(puVar21 + 6) = (char)uVar32;
  uVar25 = param_1[0x40d];
  *(char *)((long)puVar21 + 0x32) = (char)(uVar32 >> 0x10);
  *(char *)((long)puVar21 + 0x33) = (char)(uVar32 >> 0x18);
  *(char *)((long)puVar21 + 0x34) = (char)(uVar32 >> 0x20);
  pcVar28 = (code *)param_1[0x40e];
  puVar21[2] = 0;
  uVar26 = (undefined1)uVar6;
  *(undefined1 *)(puVar21 + 3) = uVar26;
  *(char *)((long)puVar21 + 0x1d) = cVar7;
  *(char *)((long)puVar21 + 0x1e) = cVar7;
                    /* WARNING: Store size is inaccurate */
  *(int *)((long)puVar21 + 0x1f) = (int)uVar6 >> 0x1f;
  *(undefined1 *)(puVar21 + 4) = uVar26;
  *(char *)((long)puVar21 + 0x25) = cVar7;
  *(char *)((long)puVar21 + 0x26) = cVar7;
                    /* WARNING: Store size is inaccurate */
  *(int *)((long)puVar21 + 0x27) = (int)uVar6 >> 0x1f;
  *(char *)(puVar21 + 5) = (char)iVar35;
  uVar14 = (undefined1)((uint)iVar35 >> 8);
  *(undefined1 *)((long)puVar21 + 0x29) = uVar14;
  uVar9 = (undefined1)((uint)iVar35 >> 0x10);
  *(undefined1 *)((long)puVar21 + 0x2a) = uVar9;
  *(char *)((long)puVar21 + 0x2b) = cVar10;
  *(char *)((long)puVar21 + 0x35) = (char)(uVar32 >> 0x28);
  *(char *)((long)puVar21 + 0x36) = (char)(uVar32 >> 0x30);
  *(char *)((long)puVar21 + 0x37) = (char)(uVar32 >> 0x38);
  uVar20 = (*pcVar28)(uVar25,puVar21,0x38);
  if ((int)uVar20 < 1) {
    uVar32 = param_1[2];
  }
  else {
    uVar32 = param_1[2] + (ulong)uVar20;
    param_1[2] = uVar32;
  }
  if (uVar32 - *param_1 >> 0x18 != 0) {
    FUN_ffff0000be011708(uVar32 >> 0x14);
    uVar32 = param_1[2];
    *param_1 = uVar32;
  }
  if (0xffff < uVar32 - param_1[1]) {
    FUN_ffff0000be0357ec();
    param_1[1] = param_1[2];
  }
  if (uVar20 == 0x38) {
    pcVar28 = (code *)param_1[0x40e];
    *(char *)((long)puVar21 + 9) = (char)(uVar34 >> 8);
    *puVar21 = 0x7064b50;
    *(char *)((long)puVar21 + 10) = (char)(uVar34 >> 0x10);
    *(char *)((long)puVar21 + 0xc) = (char)(uVar34 >> 0x20);
    *(char *)((long)puVar21 + 0xb) = (char)(uVar34 >> 0x18);
    *(char *)((long)puVar21 + 0xd) = (char)(uVar34 >> 0x28);
    uVar32 = param_1[0x40d];
    *(char *)(puVar21 + 1) = (char)uVar34;
    *(char *)((long)puVar21 + 0xe) = (char)(uVar34 >> 0x30);
    *(char *)((long)puVar21 + 0xf) = (char)(uVar34 >> 0x38);
    *(undefined4 *)(puVar21 + 2) = 1;
    uVar20 = (*pcVar28)(uVar32,puVar21,0x14);
    if ((int)uVar20 < 1) {
      uVar32 = param_1[2];
    }
    else {
      uVar32 = param_1[2] + (ulong)uVar20;
      param_1[2] = uVar32;
    }
    if (uVar32 - *param_1 >> 0x18 != 0) {
      FUN_ffff0000be011708(uVar32 >> 0x14);
      uVar32 = param_1[2];
      *param_1 = uVar32;
    }
    if (0xffff < uVar32 - param_1[1]) {
      FUN_ffff0000be0357ec();
      param_1[1] = param_1[2];
    }
    if (uVar20 == 0x14) {
      uVar32 = param_1[0x40d];
      *(undefined1 *)((long)puVar21 + 9) = uVar11;
      *puVar21 = 0x6054b50;
      *(undefined1 *)((long)puVar21 + 0xb) = uVar11;
      pcVar28 = (code *)param_1[0x40e];
      *(undefined1 *)(puVar21 + 1) = uVar26;
      *(undefined1 *)((long)puVar21 + 10) = uVar26;
      *(char *)((long)puVar21 + 0xc) = (char)iVar35;
      *(undefined1 *)((long)puVar21 + 0xd) = uVar14;
      *(undefined1 *)((long)puVar21 + 0xe) = uVar9;
      *(char *)((long)puVar21 + 0xf) = cVar10;
      *(undefined4 *)(puVar21 + 2) = 0xffffffff;
      *(undefined2 *)((long)puVar21 + 0x14) = 0;
      uVar20 = (*pcVar28)(uVar32,puVar21,0x16);
      if ((int)uVar20 < 1) {
        uVar32 = param_1[2];
      }
      else {
        uVar32 = param_1[2] + (ulong)uVar20;
        param_1[2] = uVar32;
      }
      if (uVar32 - *param_1 >> 0x18 != 0) {
        FUN_ffff0000be011708(uVar32 >> 0x14);
        uVar32 = param_1[2];
        *param_1 = uVar32;
      }
      if (0xffff < uVar32 - param_1[1]) {
        FUN_ffff0000be0357ec();
        param_1[1] = param_1[2];
      }
      if (uVar20 == 0x16) {
        if (0 < (int)uVar6) {
          puVar31 = param_1 + 4;
          do {
            if (*puVar31 != 0) {
              thunk_FUN_ffff0000be09937c();
            }
            puVar31 = puVar31 + 7;
            uVar30 = uVar30 - 1;
          } while (uVar30 != 0);
        }
        thunk_FUN_ffff0000be09937c(puVar21);
        thunk_FUN_ffff0000be09937c(param_1);
        return 1;
      }
    }
  }
LAB_ffff0000be00b7c0:
  thunk_FUN_ffff0000be09937c(puVar21);
  FUN_ffff0000be05fd98
            (s__s__d__close_zfile_fail_ffff0000be0a4280,s_kzip_close_ffff0000be0af403,0x2e5);
  thunk_FUN_ffff0000be09937c(param_1);
  return 0;
}


