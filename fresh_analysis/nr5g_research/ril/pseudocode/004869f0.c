
/* RmcNetworkRequestHandler::requestSetNrBandMode(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcNetworkRequestHandler::requestSetNrBandMode(RmcNetworkRequestHandler *this,sp *param_1)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  undefined4 uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  long lVar8;
  bool bVar9;
  int iVar10;
  void *__ptr;
  void *__ptr_00;
  undefined8 uVar11;
  char *pcVar12;
  size_t sVar13;
  long **pplVar14;
  ulong uVar15;
  long lVar16;
  long lVar17;
  uint *puVar18;
  long *plVar19;
  ulong uVar20;
  ulong uVar21;
  undefined8 *puVar22;
  int *piVar23;
  uint *puVar24;
  uint *puVar25;
  RfxAtLine *this_00;
  ulong uVar26;
  ulong __nmemb;
  ulong uVar27;
  undefined8 uVar28;
  undefined8 uVar29;
  void *local_1f8;
  void *local_1f0;
  long *local_1e8;
  long *local_1e0;
  long *local_1d8;
  undefined8 local_1d0;
  undefined8 local_1c8;
  int local_1bc;
  long *local_1b8;
  long *local_1b0;
  long *local_1a8 [3];
  char local_190 [16];
  undefined8 local_180;
  undefined8 uStack_178;
  undefined8 local_170;
  undefined8 local_168;
  undefined8 local_160;
  undefined8 uStack_158;
  undefined8 local_150;
  undefined8 local_148;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 local_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 local_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 local_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 local_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 local_88;
  long local_78;
  
  lVar8 = tpidr_el0;
  local_78 = *(long *)(lVar8 + 0x28);
  local_1b8 = (long *)0x0;
  local_1b0 = (long *)0x0;
  local_1bc = 0;
  local_190[0] = '\0';
  local_190[1] = '\0';
  local_190[2] = '\0';
  local_190[3] = '\0';
  local_190[4] = '\0';
  local_190[5] = '\0';
  local_190[6] = '\0';
  local_190[7] = '\0';
  local_1c8 = 0;
  puVar25 = *(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  uStack_158 = 0;
  local_160 = 0;
  local_148 = 0;
  local_150 = 0;
  uStack_138 = 0;
  local_140 = 0;
  local_128 = 0;
  local_130 = 0;
  uStack_118 = 0;
  local_120 = 0;
  local_108 = 0;
  local_110 = 0;
  uStack_f8 = 0;
  local_100 = 0;
  local_e8 = 0;
  local_f0 = 0;
  uStack_d8 = 0;
  local_e0 = 0;
  local_c8 = 0;
  local_d0 = 0;
  uStack_b8 = 0;
  local_c0 = 0;
  local_a8 = 0;
  local_b0 = 0;
  uStack_98 = 0;
  local_a0 = 0;
  local_88 = 0;
  local_90 = 0;
  uStack_178 = 0;
  local_180 = 0;
  local_168 = 0;
  local_170 = 0;
  local_190[8] = 0;
  android::String8::String8((String8 *)&local_1c8);
  local_1d0 = 0;
  android::String8::String8((String8 *)&local_1d0);
  uVar3 = *puVar25;
  __nmemb = (ulong)uVar3;
  if ((int)uVar3 < 1) {
    __ptr = (void *)0x0;
    uVar27 = 1;
LAB_00486bd8:
    uVar5 = puVar25[uVar27 & 0xffffffff];
    uVar26 = (ulong)uVar5;
    uVar15 = (ulong)((int)uVar27 + 1);
    if ((int)uVar5 < 1) {
      local_1f8 = (void *)0x0;
    }
    else {
      local_1f8 = calloc(uVar26,4);
      if (local_1f8 == (void *)0x0) {
        local_1f8 = (void *)0x0;
        local_1f0 = (void *)0x0;
        __ptr_00 = (void *)0x0;
        goto LAB_00487130;
      }
      if ((uVar5 < 8) ||
         ((ulong)((long)local_1f8 + (-4 - (long)(puVar25 + (uVar27 & 0xffffffff)))) < 0x20)) {
        uVar20 = 0;
        uVar27 = uVar15;
      }
      else {
        uVar20 = uVar26 & 0x7ffffff8;
        uVar27 = uVar20 + uVar15;
        puVar18 = puVar25 + uVar15 + 4;
        puVar22 = (undefined8 *)((long)local_1f8 + 0x10);
        uVar15 = uVar20;
        do {
          puVar24 = puVar18 + -2;
          uVar11 = *(undefined8 *)(puVar18 + -4);
          uVar29 = *(undefined8 *)(puVar18 + 2);
          uVar28 = *(undefined8 *)puVar18;
          puVar18 = puVar18 + 8;
          uVar15 = uVar15 - 8;
          puVar22[-1] = *(undefined8 *)puVar24;
          puVar22[-2] = uVar11;
          puVar22[1] = uVar29;
          *puVar22 = uVar28;
          puVar22 = puVar22 + 4;
        } while (uVar15 != 0);
        uVar15 = uVar27;
        if (uVar20 == uVar26) goto LAB_00486d54;
      }
      uVar15 = uVar27;
      uVar21 = uVar20;
      if ((uVar5 & 3) != 0) {
        *(uint *)((long)local_1f8 + uVar20 * 4) = puVar25[uVar27];
        uVar15 = uVar27 + 1;
        uVar21 = uVar20 | 1;
        if ((uVar26 & 3) != 1) {
          *(uint *)((long)local_1f8 + (uVar20 | 1) * 4) = puVar25[uVar27 + 1];
          uVar15 = uVar27 + 2;
          uVar21 = uVar20 | 2;
          if ((uVar26 & 3) != 2) {
            *(uint *)((long)local_1f8 + (uVar20 | 2) * 4) = puVar25[uVar27 + 2];
            uVar15 = uVar27 + 3;
            uVar21 = uVar20 | 3;
          }
        }
      }
      if (uVar20 - uVar26 < 0xfffffffffffffffd) {
        lVar16 = uVar26 - uVar21;
        puVar18 = (uint *)((long)local_1f8 + uVar21 * 4 + 8);
        puVar24 = puVar25 + uVar15 + 2;
        do {
          uVar15 = uVar15 + 4;
          lVar16 = lVar16 + -4;
          puVar18[-2] = puVar24[-2];
          puVar18[-1] = puVar24[-1];
          *puVar18 = *puVar24;
          puVar1 = puVar24 + 1;
          puVar24 = puVar24 + 4;
          puVar18[1] = *puVar1;
          puVar18 = puVar18 + 4;
        } while (lVar16 != 0);
      }
    }
LAB_00486d54:
    uVar6 = puVar25[(int)uVar15];
    uVar27 = (ulong)uVar6;
    iVar10 = (int)uVar15 + 1;
    if ((int)uVar6 < 1) {
      local_1f0 = (void *)0x0;
    }
    else {
      local_1f0 = calloc(uVar27,4);
      if (local_1f0 == (void *)0x0) {
        local_1f0 = (void *)0x0;
        __ptr_00 = (void *)0x0;
        goto LAB_00487130;
      }
      lVar16 = (long)iVar10;
      if ((uVar6 < 8) || ((ulong)((long)local_1f0 + (lVar16 * -4 - (long)puVar25)) < 0x20)) {
        uVar15 = 0;
LAB_00486d98:
        lVar17 = lVar16;
        uVar20 = uVar15;
        if ((uVar6 & 3) != 0) {
          *(uint *)((long)local_1f0 + uVar15 * 4) = puVar25[lVar16];
          lVar17 = lVar16 + 1;
          uVar20 = uVar15 | 1;
          if ((uVar27 & 3) != 1) {
            *(uint *)((long)local_1f0 + (uVar15 | 1) * 4) = puVar25[lVar16 + 1];
            lVar17 = lVar16 + 2;
            uVar20 = uVar15 | 2;
            if ((uVar27 & 3) != 2) {
              *(uint *)((long)local_1f0 + (uVar15 | 2) * 4) = puVar25[lVar16 + 2];
              lVar17 = lVar16 + 3;
              uVar20 = uVar15 | 3;
            }
          }
        }
        if (uVar15 - uVar27 < 0xfffffffffffffffd) {
          lVar16 = uVar27 - uVar20;
          puVar18 = (uint *)((long)local_1f0 + uVar20 * 4 + 8);
          puVar24 = puVar25 + lVar17 + 2;
          do {
            lVar17 = lVar17 + 4;
            lVar16 = lVar16 + -4;
            puVar18[-2] = puVar24[-2];
            puVar18[-1] = puVar24[-1];
            *puVar18 = *puVar24;
            puVar1 = puVar24 + 1;
            puVar24 = puVar24 + 4;
            puVar18[1] = *puVar1;
            puVar18 = puVar18 + 4;
          } while (lVar16 != 0);
        }
      }
      else {
        uVar15 = uVar27 & 0x7ffffff8;
        lVar17 = uVar15 + lVar16;
        puVar18 = puVar25 + lVar16 + 4;
        puVar22 = (undefined8 *)((long)local_1f0 + 0x10);
        uVar20 = uVar15;
        do {
          puVar24 = puVar18 + -2;
          uVar11 = *(undefined8 *)(puVar18 + -4);
          uVar29 = *(undefined8 *)(puVar18 + 2);
          uVar28 = *(undefined8 *)puVar18;
          puVar18 = puVar18 + 8;
          uVar20 = uVar20 - 8;
          puVar22[-1] = *(undefined8 *)puVar24;
          puVar22[-2] = uVar11;
          puVar22[1] = uVar29;
          *puVar22 = uVar28;
          puVar22 = puVar22 + 4;
        } while (uVar20 != 0);
        lVar16 = lVar17;
        if (uVar15 != uVar27) goto LAB_00486d98;
      }
      iVar10 = (int)lVar17;
    }
    uVar7 = puVar25[iVar10];
    uVar15 = (ulong)uVar7;
    if ((int)uVar7 < 1) {
      __ptr_00 = (void *)0x0;
    }
    else {
      __ptr_00 = calloc(uVar15,4);
      if (__ptr_00 == (void *)0x0) goto LAB_00487130;
      lVar16 = (long)iVar10;
      if (uVar7 < 8) {
        uVar21 = 0;
      }
      else if ((ulong)((long)__ptr_00 + (-4 - (long)(puVar25 + lVar16))) < 0x20) {
        uVar21 = 0;
      }
      else {
        uVar21 = uVar15 & 0x7ffffff8;
        puVar18 = puVar25 + lVar16 + 5;
        lVar16 = uVar21 + lVar16;
        puVar22 = (undefined8 *)((long)__ptr_00 + 0x10);
        uVar20 = uVar21;
        do {
          puVar24 = puVar18 + -2;
          uVar11 = *(undefined8 *)(puVar18 + -4);
          uVar29 = *(undefined8 *)(puVar18 + 2);
          uVar28 = *(undefined8 *)puVar18;
          puVar18 = puVar18 + 8;
          uVar20 = uVar20 - 8;
          puVar22[-1] = *(undefined8 *)puVar24;
          puVar22[-2] = uVar11;
          puVar22[1] = uVar29;
          *puVar22 = uVar28;
          puVar22 = puVar22 + 4;
        } while (uVar20 != 0);
        if (uVar21 == uVar15) goto LAB_00486fec;
      }
      lVar17 = lVar16;
      uVar20 = uVar21;
      if ((uVar7 & 3) != 0) {
        lVar17 = lVar16 + 1;
        uVar20 = uVar21 | 1;
        *(uint *)((long)__ptr_00 + uVar21 * 4) = puVar25[lVar17];
        if ((uVar15 & 3) != 1) {
          lVar17 = lVar16 + 2;
          *(uint *)((long)__ptr_00 + uVar20 * 4) = puVar25[lVar17];
          uVar20 = uVar21 | 2;
          if ((uVar15 & 3) != 2) {
            *(uint *)((long)__ptr_00 + uVar20 * 4) = puVar25[lVar16 + 3];
            lVar17 = lVar16 + 3;
            uVar20 = uVar21 | 3;
          }
        }
      }
      if (uVar21 - uVar15 < 0xfffffffffffffffd) {
        lVar16 = uVar15 - uVar20;
        puVar18 = (uint *)((long)__ptr_00 + uVar20 * 4 + 8);
        puVar25 = puVar25 + lVar17;
        do {
          lVar16 = lVar16 + -4;
          puVar18[-2] = puVar25[1];
          puVar18[-1] = puVar25[2];
          *puVar18 = puVar25[3];
          puVar18[1] = puVar25[4];
          puVar18 = puVar18 + 4;
          puVar25 = puVar25 + 4;
        } while (lVar16 != 0);
      }
    }
LAB_00486fec:
    RfxBaseHandler::atSendCommandSingleline((char *)this,"AT+EPBSEH?","+EPBSEH:");
    plVar19 = local_1b8;
    if (local_1b8 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_1b8 + *(long *)(*local_1b8 + -0x18)),&local_1b8);
    }
    if (plVar19 != local_1b8) {
      android::sp_report_race();
    }
    local_1bc = (int)local_1a8[0][5];
    local_1b8 = local_1a8[0];
    if ((((local_1bc != 0) || (*(int *)((long)local_1a8[0] + 0xc) == 0)) ||
        (this_00 = (RfxAtLine *)local_1a8[0][4], this_00 == (RfxAtLine *)0x0)) ||
       ((RfxAtLine::atTokStart(this_00,&local_1bc), local_1bc < 0 ||
        (uVar11 = RfxAtLine::atTokNextstr(this_00,&local_1bc), local_1bc < 0)))) goto LAB_00487130;
    RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","gsmBand = %s",uVar11);
    uVar11 = RfxAtLine::atTokNextstr(this_00,&local_1bc);
    if (local_1bc < 0) goto LAB_00487130;
    RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","umtsBand = %s",uVar11);
    RfxAtLine::atTokNextstr(this_00,&local_1bc);
    if (((local_1bc < 0) ||
        (pcVar12 = (char *)RfxAtLine::atTokNextstr(this_00,&local_1bc), pcVar12 == (char *)0x0)) ||
       (local_1bc < 0)) goto LAB_00487130;
    sVar13 = strlen(pcVar12);
    if (((int)sVar13 == 0) || ((sVar13 & 7) != 0)) goto LAB_00487130;
    uVar2 = (int)sVar13 >> 3;
    if (0x20 < (int)uVar2) goto LAB_00487130;
    RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","saBands = %s. len = %d",pcVar12);
    if (0 < (int)uVar2) {
      uVar20 = 0;
      do {
        strncpy(local_190,pcVar12,8);
        local_190[8] = 0;
        lVar16 = strtol(local_190,(char **)0x0,0x10);
        *(int *)((long)&local_100 + uVar20 * 4) = (int)lVar16;
        RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","saBands[%d] = %d",uVar20 & 0xffffffff);
        uVar20 = uVar20 + 1;
        pcVar12 = pcVar12 + 8;
      } while (uVar2 != uVar20);
    }
    iVar10 = RfxAtLine::atTokHasmore(this_00);
    if (iVar10 != 0) {
      pcVar12 = (char *)RfxAtLine::atTokNextstr(this_00,&local_1bc);
      if ((pcVar12 != (char *)0x0) && (-1 < local_1bc)) {
        sVar13 = strlen(pcVar12);
        if (((int)sVar13 != 0) && ((sVar13 & 7) == 0)) {
          uVar2 = (int)sVar13 >> 3;
          if ((int)uVar2 < 0x21) {
            RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","nsaBands = %s. len = %d",pcVar12);
            if (0 < (int)uVar2) {
              uVar20 = 0;
              do {
                strncpy(local_190,pcVar12,8);
                local_190[8] = 0;
                lVar16 = strtol(local_190,(char **)0x0,0x10);
                *(int *)((long)&local_180 + uVar20 * 4) = (int)lVar16;
                RfxBaseHandler::logE
                          ((char *)this,"RmcNwReqHdlr","nsaBands[%d] = %d",uVar20 & 0xffffffff);
                uVar20 = uVar20 + 1;
                pcVar12 = pcVar12 + 8;
              } while (uVar2 != uVar20);
            }
            goto LAB_00487434;
          }
        }
      }
      goto LAB_00487130;
    }
    uStack_138 = uStack_b8;
    local_140 = local_c0;
    local_128 = local_a8;
    local_130 = local_b0;
    uStack_118 = uStack_98;
    local_120 = local_a0;
    local_108 = local_88;
    local_110 = local_90;
    uStack_178 = uStack_f8;
    local_180 = local_100;
    local_168 = local_e8;
    local_170 = local_f0;
    uStack_158 = uStack_d8;
    local_160 = local_e0;
    local_148 = local_c8;
    local_150 = local_d0;
LAB_00487434:
    if (0 < (int)uVar3) {
      if (uVar3 < 4) {
        lVar16 = 0;
      }
      else {
        lVar16 = 0;
        piVar23 = (int *)((long)__ptr + 8);
        do {
          uVar2 = piVar23[-2] - 1;
          if (uVar2 < 0x400) {
            lVar17 = (ulong)(uVar2 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) | 1 << (ulong)(uVar2 & 0x1f);
          }
          uVar2 = piVar23[-1] - 1;
          if (uVar2 < 0x400) {
            lVar17 = (ulong)(uVar2 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) | 1 << (ulong)(uVar2 & 0x1f);
          }
          uVar2 = *piVar23 - 1;
          if (uVar2 < 0x400) {
            lVar17 = (ulong)(uVar2 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) | 1 << (ulong)(uVar2 & 0x1f);
          }
          uVar2 = piVar23[1] - 1;
          if (uVar2 < 0x400) {
            lVar17 = (ulong)(uVar2 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) | 1 << (ulong)(uVar2 & 0x1f);
          }
          piVar23 = piVar23 + 4;
          lVar16 = lVar16 + -4;
        } while (-lVar16 != (__nmemb & 0x7ffffffc));
        lVar16 = -lVar16;
      }
      if ((uVar3 & 3) != 0) {
        uVar3 = *(int *)((long)__ptr + lVar16 * 4) - 1;
        if (uVar3 < 0x400) {
          lVar17 = (ulong)(uVar3 >> 5) * 4;
          *(uint *)((long)&local_100 + lVar17) =
               *(uint *)((long)&local_100 + lVar17) | 1 << (ulong)(uVar3 & 0x1f);
        }
        if ((__nmemb & 3) != 1) {
          uVar3 = *(int *)((long)__ptr + lVar16 * 4 + 4) - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) | 1 << (ulong)(uVar3 & 0x1f);
          }
          if (((__nmemb & 3) != 2) &&
             (uVar3 = *(int *)((long)__ptr + lVar16 * 4 + 8) - 1, uVar3 < 0x400)) {
            lVar16 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar16) =
                 *(uint *)((long)&local_100 + lVar16) | 1 << (ulong)(uVar3 & 0x1f);
          }
        }
      }
    }
    if (0 < (int)uVar5) {
      if (uVar5 < 4) {
        lVar16 = 0;
      }
      else {
        lVar16 = 0;
        piVar23 = (int *)((long)local_1f8 + 8);
        do {
          uVar3 = piVar23[-2] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          uVar3 = piVar23[-1] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          uVar3 = *piVar23 - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          uVar3 = piVar23[1] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          piVar23 = piVar23 + 4;
          lVar16 = lVar16 + -4;
        } while (-lVar16 != (uVar26 & 0x7ffffffc));
        lVar16 = -lVar16;
      }
      if ((uVar5 & 3) != 0) {
        uVar3 = *(int *)((long)local_1f8 + lVar16 * 4) - 1;
        if (uVar3 < 0x400) {
          lVar17 = (ulong)(uVar3 >> 5) * 4;
          *(uint *)((long)&local_100 + lVar17) =
               *(uint *)((long)&local_100 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
        }
        if ((uVar26 & 3) != 1) {
          uVar3 = *(int *)((long)local_1f8 + lVar16 * 4 + 4) - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar17) =
                 *(uint *)((long)&local_100 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          if (((uVar26 & 3) != 2) &&
             (uVar3 = *(int *)((long)local_1f8 + lVar16 * 4 + 8) - 1, uVar3 < 0x400)) {
            lVar16 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_100 + lVar16) =
                 *(uint *)((long)&local_100 + lVar16) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
        }
      }
    }
    if (0 < (int)uVar6) {
      if (uVar6 < 4) {
        lVar16 = 0;
      }
      else {
        lVar16 = 0;
        piVar23 = (int *)((long)local_1f0 + 8);
        do {
          uVar3 = piVar23[-2] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) | 1 << (ulong)(uVar3 & 0x1f);
          }
          uVar3 = piVar23[-1] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) | 1 << (ulong)(uVar3 & 0x1f);
          }
          uVar3 = *piVar23 - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) | 1 << (ulong)(uVar3 & 0x1f);
          }
          uVar3 = piVar23[1] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) | 1 << (ulong)(uVar3 & 0x1f);
          }
          piVar23 = piVar23 + 4;
          lVar16 = lVar16 + -4;
        } while (-lVar16 != (uVar27 & 0x7ffffffc));
        lVar16 = -lVar16;
      }
      if ((uVar6 & 3) != 0) {
        uVar3 = *(int *)((long)local_1f0 + lVar16 * 4) - 1;
        if (uVar3 < 0x400) {
          lVar17 = (ulong)(uVar3 >> 5) * 4;
          *(uint *)((long)&local_180 + lVar17) =
               *(uint *)((long)&local_180 + lVar17) | 1 << (ulong)(uVar3 & 0x1f);
        }
        if ((uVar27 & 3) != 1) {
          uVar3 = *(int *)((long)local_1f0 + lVar16 * 4 + 4) - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) | 1 << (ulong)(uVar3 & 0x1f);
          }
          if (((uVar27 & 3) != 2) &&
             (uVar3 = *(int *)((long)local_1f0 + lVar16 * 4 + 8) - 1, uVar3 < 0x400)) {
            lVar16 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar16) =
                 *(uint *)((long)&local_180 + lVar16) | 1 << (ulong)(uVar3 & 0x1f);
          }
        }
      }
    }
    if (0 < (int)uVar7) {
      if (uVar7 < 4) {
        lVar16 = 0;
      }
      else {
        lVar16 = 0;
        piVar23 = (int *)((long)__ptr_00 + 8);
        do {
          uVar3 = piVar23[-2] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          uVar3 = piVar23[-1] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          uVar3 = *piVar23 - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          uVar3 = piVar23[1] - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          piVar23 = piVar23 + 4;
          lVar16 = lVar16 + -4;
        } while (-lVar16 != (uVar15 & 0x7ffffffc));
        lVar16 = -lVar16;
      }
      if ((uVar7 & 3) != 0) {
        uVar3 = *(int *)((long)__ptr_00 + lVar16 * 4) - 1;
        if (uVar3 < 0x400) {
          lVar17 = (ulong)(uVar3 >> 5) * 4;
          *(uint *)((long)&local_180 + lVar17) =
               *(uint *)((long)&local_180 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
        }
        if ((uVar15 & 3) != 1) {
          uVar3 = *(int *)((long)__ptr_00 + lVar16 * 4 + 4) - 1;
          if (uVar3 < 0x400) {
            lVar17 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar17) =
                 *(uint *)((long)&local_180 + lVar17) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
          if (((uVar15 & 3) != 2) &&
             (uVar3 = *(int *)((long)__ptr_00 + lVar16 * 4 + 8) - 1, uVar3 < 0x400)) {
            lVar16 = (ulong)(uVar3 >> 5) * 4;
            *(uint *)((long)&local_180 + lVar16) =
                 *(uint *)((long)&local_180 + lVar16) & (1 << (ulong)(uVar3 & 0x1f) ^ 0xffffffffU);
          }
        }
      }
    }
    if (local_88._4_4_ == 0) {
      if ((int)local_88 != 0) {
        bVar9 = false;
        lVar16 = 0x1f;
        goto LAB_00487d1c;
      }
      if (local_90._4_4_ != 0) {
        lVar16 = 0x1e;
LAB_00487d18:
        bVar9 = false;
        goto LAB_00487d1c;
      }
      if ((int)local_90 != 0) {
        lVar16 = 0x1d;
        goto LAB_00487d18;
      }
      if (uStack_98._4_4_ != 0) {
        lVar16 = 0x1c;
        goto LAB_00487d18;
      }
      if ((int)uStack_98 != 0) {
        lVar16 = 0x1b;
        goto LAB_00487d18;
      }
      if (local_a0._4_4_ != 0) {
        lVar16 = 0x1a;
        goto LAB_00487d18;
      }
      if ((int)local_a0 != 0) {
        lVar16 = 0x19;
        goto LAB_00487d18;
      }
      if (local_a8._4_4_ != 0) {
        lVar16 = 0x18;
        goto LAB_00487d18;
      }
      if ((int)local_a8 != 0) {
        lVar16 = 0x17;
        goto LAB_00487d18;
      }
      if (local_b0._4_4_ != 0) {
        lVar16 = 0x16;
        goto LAB_00487d18;
      }
      if ((int)local_b0 != 0) {
        lVar16 = 0x15;
        goto LAB_00487d18;
      }
      if (uStack_b8._4_4_ != 0) {
        lVar16 = 0x14;
        goto LAB_00487d18;
      }
      if ((int)uStack_b8 != 0) {
        lVar16 = 0x13;
        goto LAB_00487d18;
      }
      if (local_c0._4_4_ != 0) {
        lVar16 = 0x12;
        goto LAB_00487d18;
      }
      if ((int)local_c0 != 0) {
        lVar16 = 0x11;
        goto LAB_00487d18;
      }
      if (local_c8._4_4_ != 0) {
        lVar16 = 0x10;
        goto LAB_00487d18;
      }
      if ((int)local_c8 != 0) {
        lVar16 = 0xf;
        goto LAB_00487d18;
      }
      if (local_d0._4_4_ != 0) {
        lVar16 = 0xe;
        goto LAB_00487d18;
      }
      if ((int)local_d0 != 0) {
        lVar16 = 0xd;
        goto LAB_00487d18;
      }
      if (uStack_d8._4_4_ != 0) {
        lVar16 = 0xc;
        goto LAB_00487d18;
      }
      if ((int)uStack_d8 != 0) {
        lVar16 = 0xb;
        goto LAB_00487d18;
      }
      if (local_e0._4_4_ != 0) {
        lVar16 = 10;
        goto LAB_00487d18;
      }
      if ((int)local_e0 != 0) {
        lVar16 = 9;
        goto LAB_00487d18;
      }
      if (local_e8._4_4_ != 0) {
        lVar16 = 8;
        goto LAB_00487d18;
      }
      if ((int)local_e8 != 0) {
        lVar16 = 7;
        goto LAB_00487d18;
      }
      if (local_f0._4_4_ != 0) {
        lVar16 = 6;
        goto LAB_00487d18;
      }
      if ((int)local_f0 != 0) {
        lVar16 = 5;
        goto LAB_00487d18;
      }
      if (uStack_f8._4_4_ != 0) {
        lVar16 = 4;
        goto LAB_00487d18;
      }
      if ((int)uStack_f8 != 0) {
        lVar16 = 3;
        goto LAB_00487d18;
      }
      if (local_100._4_4_ != 0) {
        lVar16 = 2;
        goto LAB_00487d18;
      }
      if ((int)local_100 != 0) {
        bVar9 = true;
        lVar16 = 1;
        goto LAB_00487d1c;
      }
    }
    else {
      bVar9 = false;
      lVar16 = 0x20;
LAB_00487d1c:
      android::String8::format("%08X",local_1a8,local_100 & 0xffffffff);
      android::String8::setTo((String8 *)&local_1c8);
      android::String8::~String8((String8 *)local_1a8);
      if (!bVar9) {
        lVar16 = lVar16 + -1;
        puVar25 = (uint *)((ulong)&local_100 | 4);
        do {
          android::String8::format("%s%08X",local_1a8,local_1c8,(ulong)*puVar25);
          android::String8::setTo((String8 *)&local_1c8);
          android::String8::~String8((String8 *)local_1a8);
          lVar16 = lVar16 + -1;
          puVar25 = puVar25 + 1;
        } while (lVar16 != 0);
      }
    }
    if (local_108._4_4_ == 0) {
      if ((int)local_108 != 0) {
        bVar9 = false;
        lVar16 = 0x1f;
        goto LAB_00488098;
      }
      if (local_110._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0x1e;
        goto LAB_00488098;
      }
      if ((int)local_110 != 0) {
        bVar9 = false;
        lVar16 = 0x1d;
        goto LAB_00488098;
      }
      if (uStack_118._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0x1c;
        goto LAB_00488098;
      }
      if ((int)uStack_118 != 0) {
        bVar9 = false;
        lVar16 = 0x1b;
        goto LAB_00488098;
      }
      if (local_120._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0x1a;
        goto LAB_00488098;
      }
      if ((int)local_120 != 0) {
        bVar9 = false;
        lVar16 = 0x19;
        goto LAB_00488098;
      }
      if (local_128._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0x18;
        goto LAB_00488098;
      }
      if ((int)local_128 != 0) {
        bVar9 = false;
        lVar16 = 0x17;
        goto LAB_00488098;
      }
      if (local_130._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0x16;
        goto LAB_00488098;
      }
      if ((int)local_130 != 0) {
        bVar9 = false;
        lVar16 = 0x15;
        goto LAB_00488098;
      }
      if (uStack_138._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0x14;
        goto LAB_00488098;
      }
      if ((int)uStack_138 != 0) {
        bVar9 = false;
        lVar16 = 0x13;
        goto LAB_00488098;
      }
      if (local_140._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0x12;
        goto LAB_00488098;
      }
      if ((int)local_140 != 0) {
        bVar9 = false;
        lVar16 = 0x11;
        goto LAB_00488098;
      }
      if (local_148._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0x10;
        goto LAB_00488098;
      }
      if ((int)local_148 != 0) {
        bVar9 = false;
        lVar16 = 0xf;
        goto LAB_00488098;
      }
      if (local_150._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0xe;
        goto LAB_00488098;
      }
      if ((int)local_150 != 0) {
        bVar9 = false;
        lVar16 = 0xd;
        goto LAB_00488098;
      }
      if (uStack_158._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 0xc;
        goto LAB_00488098;
      }
      if ((int)uStack_158 != 0) {
        bVar9 = false;
        lVar16 = 0xb;
        goto LAB_00488098;
      }
      if (local_160._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 10;
        goto LAB_00488098;
      }
      if ((int)local_160 != 0) {
        bVar9 = false;
        lVar16 = 9;
        goto LAB_00488098;
      }
      if (local_168._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 8;
        goto LAB_00488098;
      }
      if ((int)local_168 != 0) {
        bVar9 = false;
        lVar16 = 7;
        goto LAB_00488098;
      }
      if (local_170._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 6;
        goto LAB_00488098;
      }
      if ((int)local_170 != 0) {
        bVar9 = false;
        lVar16 = 5;
        goto LAB_00488098;
      }
      if (uStack_178._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 4;
        goto LAB_00488098;
      }
      if ((int)uStack_178 != 0) {
        bVar9 = false;
        lVar16 = 3;
        goto LAB_00488098;
      }
      if (local_180._4_4_ != 0) {
        bVar9 = false;
        lVar16 = 2;
        goto LAB_00488098;
      }
      if ((int)local_180 != 0) {
        bVar9 = true;
        lVar16 = 1;
        goto LAB_00488098;
      }
    }
    else {
      bVar9 = false;
      lVar16 = 0x20;
LAB_00488098:
      android::String8::format("%08X",local_1a8,local_180 & 0xffffffff);
      android::String8::setTo((String8 *)&local_1d0);
      android::String8::~String8((String8 *)local_1a8);
      if (!bVar9) {
        lVar16 = lVar16 + -1;
        puVar25 = (uint *)((ulong)&local_180 | 4);
        do {
          android::String8::format("%s%08X",local_1a8,local_1d0,(ulong)*puVar25);
          android::String8::setTo((String8 *)&local_1d0);
          android::String8::~String8((String8 *)local_1a8);
          lVar16 = lVar16 + -1;
          puVar25 = puVar25 + 1;
        } while (lVar16 != 0);
      }
    }
    RfxBaseHandler::logE
              ((char *)this,"RmcNwReqHdlr","saBandStr[%s], nsaBandsStr[%s]",local_1c8,local_1d0);
    android::String8::format("AT+EPBSEH=,,,\"%s\",,\"%s\"",&local_1d8,local_1c8,local_1d0);
    RfxBaseHandler::atSendCommand((String8 *)this,&local_1d8);
    android::sp<RfxAtResponse>::operator=((sp<RfxAtResponse> *)&local_1b8,(sp *)local_1a8);
    if (local_1a8[0] != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_1a8[0] + *(long *)(*local_1a8[0] + -0x18)),local_1a8);
    }
    android::String8::~String8((String8 *)&local_1d8);
    local_1bc = (int)local_1b8[5];
    if ((local_1bc < 0) || (*(int *)((long)local_1b8 + 0xc) == 0)) goto LAB_00487130;
    uVar4 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)local_1a8);
    local_1e0 = *(long **)param_1;
    if (local_1e0 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_1e0 + *(long *)(*local_1e0 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_1d8,uVar4,0,local_1a8,&local_1e0,0);
    android::sp<RfxMclMessage>::operator=((sp<RfxMclMessage> *)&local_1b0,(sp *)&local_1d8);
    if (local_1d8 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_1d8 + *(long *)(*local_1d8 + -0x18)),&local_1d8);
    }
    if (local_1e0 == (long *)0x0) goto LAB_004871e8;
    lVar16 = *local_1e0;
    pplVar14 = &local_1e0;
    plVar19 = local_1e0;
  }
  else {
    __ptr = calloc(__nmemb,4);
    if (__ptr != (void *)0x0) {
      if ((uVar3 < 8) || ((ulong)((long)__ptr + (-4 - (long)puVar25)) < 0x20)) {
        uVar15 = 0;
        uVar26 = 1;
      }
      else {
        uVar15 = __nmemb & 0x7ffffff8;
        puVar18 = puVar25 + 5;
        uVar26 = uVar15 | 1;
        puVar22 = (undefined8 *)((long)__ptr + 0x10);
        uVar27 = uVar15;
        do {
          puVar24 = puVar18 + -2;
          uVar11 = *(undefined8 *)(puVar18 + -4);
          uVar29 = *(undefined8 *)(puVar18 + 2);
          uVar28 = *(undefined8 *)puVar18;
          puVar18 = puVar18 + 8;
          uVar27 = uVar27 - 8;
          puVar22[-1] = *(undefined8 *)puVar24;
          puVar22[-2] = uVar11;
          puVar22[1] = uVar29;
          *puVar22 = uVar28;
          puVar22 = puVar22 + 4;
        } while (uVar27 != 0);
        uVar27 = uVar26;
        if (uVar15 == __nmemb) goto LAB_00486bd8;
      }
      uVar27 = uVar26;
      uVar20 = uVar15;
      if ((uVar3 & 3) != 0) {
        *(uint *)((long)__ptr + uVar15 * 4) = puVar25[uVar26];
        uVar27 = uVar26 + 1;
        uVar20 = uVar15 | 1;
        if ((__nmemb & 3) != 1) {
          *(uint *)((long)__ptr + (uVar15 | 1) * 4) = puVar25[uVar26 + 1];
          uVar27 = uVar26 + 2;
          uVar20 = uVar15 | 2;
          if ((__nmemb & 3) != 2) {
            *(uint *)((long)__ptr + (uVar15 | 2) * 4) = puVar25[uVar26 + 2];
            uVar27 = uVar26 + 3;
            uVar20 = uVar15 | 3;
          }
        }
      }
      if (uVar15 - __nmemb < 0xfffffffffffffffd) {
        lVar16 = __nmemb - uVar20;
        puVar18 = (uint *)((long)__ptr + uVar20 * 4 + 8);
        puVar24 = puVar25 + uVar27 + 2;
        do {
          uVar27 = uVar27 + 4;
          lVar16 = lVar16 + -4;
          puVar18[-2] = puVar24[-2];
          puVar18[-1] = puVar24[-1];
          *puVar18 = *puVar24;
          puVar1 = puVar24 + 1;
          puVar24 = puVar24 + 4;
          puVar18[1] = *puVar1;
          puVar18 = puVar18 + 4;
        } while (lVar16 != 0);
      }
      goto LAB_00486bd8;
    }
    local_1f8 = (void *)0x0;
    __ptr_00 = (void *)0x0;
    local_1f0 = (void *)0x0;
LAB_00487130:
    RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","There is something wrong with AT+EPBSEH");
    uVar4 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)local_1a8);
    local_1e8 = *(long **)param_1;
    if (local_1e8 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_1e8 + *(long *)(*local_1e8 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_1d8,uVar4,2,local_1a8,&local_1e8,0);
    plVar19 = local_1b0;
    if (local_1b0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_1b0 + *(long *)(*local_1b0 + -0x18)),&local_1b0);
    }
    if (plVar19 != local_1b0) {
      android::sp_report_race();
    }
    local_1b0 = local_1d8;
    local_1d8 = (long *)0x0;
    if (local_1e8 == (long *)0x0) goto LAB_004871e8;
    lVar16 = *local_1e8;
    pplVar14 = &local_1e8;
    plVar19 = local_1e8;
  }
  android::RefBase::decStrong((RefBase *)((long)plVar19 + *(long *)(lVar16 + -0x18)),pplVar14);
LAB_004871e8:
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_1a8);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_1b0);
  if (__ptr != (void *)0x0) {
    free(__ptr);
  }
  if (local_1f8 != (void *)0x0) {
    free(local_1f8);
  }
  if (local_1f0 != (void *)0x0) {
    free(local_1f0);
  }
  if (__ptr_00 != (void *)0x0) {
    free(__ptr_00);
  }
  android::String8::~String8((String8 *)&local_1d0);
  android::String8::~String8((String8 *)&local_1c8);
  if (local_1b8 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_1b8 + *(long *)(*local_1b8 + -0x18)),&local_1b8);
  }
  if (local_1b0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_1b0 + *(long *)(*local_1b0 + -0x18)),&local_1b0);
  }
  if (*(long *)(lVar8 + 0x28) != local_78) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


