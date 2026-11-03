// File offset entry: 0xa5cec

/* WARNING: Possible PIC construction at 0x020a5f14: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a5f58: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a5f18) */
/* WARNING: Removing unreachable block (ram,0x020a5f1c) */
/* WARNING: Removing unreachable block (ram,0x020a5f2c) */
/* WARNING: Removing unreachable block (ram,0x020a5f5c) */

ulong FUN_020a5bec(void)

{
  undefined1 *puVar1;
  uint uVar2;
  uint *puVar3;
  int iVar4;
  undefined4 uVar5;
  ulong uVar6;
  ulong uVar7;
  undefined8 uVar8;
  char *pcVar9;
  long lVar10;
  ulong uVar11;
  char *pcVar12;
  ushort uVar13;
  char *pcVar14;
  long lVar15;
  ulong unaff_x19;
  undefined8 unaff_x20;
  undefined1 *unaff_x29;
  undefined8 unaff_x30;
  undefined1 auStack_50 [4];
  uint uStack_4c;
  undefined8 uStack_48;
  undefined4 uStack_40;
  undefined4 uStack_3c;
  long lStack_38;
  
  puVar1 = &stack0xffffffffffffffd0;
  lStack_38 = *plRam0000000002103280;
  uStack_4c = 0x20c;
  uStack_3c = 0;
  uStack_48 = 0;
  uRam0000000002123ae0 = uRam0000000002123840;
  iRam0000000002123ae8 = iRam0000000002123850;
  if (iRam0000000002123850 != 0) {
    do {
      iVar4 = func_0x020a6a40(2);
      if (iVar4 != 0) break;
      uStack_40 = 0;
      iVar4 = func_0x020a6c9c(3,&uStack_48,&uStack_3c,&uStack_40,0);
      if ((iVar4 == 0) &&
         (iVar4 = func_0x020a6de8(uStack_48,0x21244f4,0x40c,&uStack_4c), iVar4 == 0)) {
        uVar7 = (ulong)uStack_4c;
        uRam0000000002123ae0 = 0;
        iRam0000000002123ae8 = 0;
        if (sRam00000000021244fc != 0) {
          uVar6 = 0x101013;
          uVar2 = uRam00000000021244f0 + 1 >> 1;
          if (uVar2 != uRam00000000021244f4) goto LAB_020a5cb0;
          if (uRam00000000021244f0 + 1 < 2) goto LAB_020a5d98;
          lVar10 = 0;
          lVar15 = -0x100000000;
          goto LAB_020a5d48;
        }
        uVar6 = 0x101013;
        uVar2 = *puRam0000000002103810;
        if (2 < uVar2) {
          uVar6 = 0x10101b;
          goto LAB_020a5cb0;
        }
        uVar11 = (ulong)uRam00000000021244f4;
        if (uRam00000000021244f0 != uRam00000000021244f4) goto LAB_020a5cb0;
        uVar6 = (ulong)uRam00000000021244f8;
        pcVar9 = *(char **)((long)(int)uVar2 * 8 + 0x2100f18);
        if (uRam00000000021244f4 == 0) goto LAB_020a5ed4;
        pcVar14 = *(char **)((long)(int)uVar2 * 8 + 0x2100f00);
        pcVar12 = (char *)0x21242f0;
        goto LAB_020a5eb4;
      }
    } while (iRam0000000002123ae8 != 0);
  }
  uVar6 = 0x101012;
  goto LAB_020a5cb0;
  while( true ) {
    lVar15 = lVar15 + 0x200000000;
    lVar10 = lVar10 + 2;
    if ((ulong)uVar2 << 1 == lVar10) break;
LAB_020a5d48:
    if ((uRam00000000021244f0 & 1) == 0) {
      uVar13 = *(ushort *)(lVar10 + 0x21242f0) >> 8 | *(ushort *)(lVar10 + 0x21242f0) << 8;
    }
    else {
      uVar13 = (ushort)bRam00000000021242f0;
      if (lVar10 != 0) {
        uVar13 = CONCAT11(*(undefined1 *)((lVar15 >> 0x20) + 0x21242f0),
                          *(undefined1 *)(lVar10 + 0x21242f0));
      }
    }
    if (*(ushort *)(lVar10 + 0x2124500) != uVar13) goto LAB_020a5f70;
  }
LAB_020a5d98:
  uVar2 = uRam00000000021242ec + 1 >> 1;
  if (uVar2 == uRam00000000021244f8) {
    if (1 < uRam00000000021242ec + 1) {
      lVar10 = 0;
      lVar15 = -0x100000000;
      do {
        if ((uRam00000000021242ec & 1) == 0) {
          uVar13 = *(ushort *)(lVar10 + 0x21240ec) >> 8 | *(ushort *)(lVar10 + 0x21240ec) << 8;
        }
        else {
          uVar13 = (ushort)bRam00000000021240ec;
          if (lVar10 != 0) {
            uVar13 = CONCAT11(*(undefined1 *)((lVar15 >> 0x20) + 0x21240ec),
                              *(undefined1 *)(lVar10 + 0x21240ec));
          }
        }
        if (*(ushort *)(lVar10 + 0x2124600) != uVar13) goto LAB_020a5f78;
        lVar15 = lVar15 + 0x200000000;
        lVar10 = lVar10 + 2;
      } while ((ulong)uVar2 << 1 != lVar10);
    }
    if (*plRam0000000002103280 == lStack_38) goto code_r0x020a714c;
    goto LAB_020a5f80;
  }
  goto LAB_020a5f68;
LAB_020a5f70:
  uVar6 = 0x101014;
  goto LAB_020a5cb0;
LAB_020a5f78:
  uVar6 = 0x101016;
  goto LAB_020a5cb0;
  while( true ) {
    uVar11 = uVar11 - 1;
    pcVar14 = pcVar14 + 1;
    pcVar12 = pcVar12 + 1;
    if (uVar11 == 0) break;
LAB_020a5eb4:
    if (*pcVar12 != *pcVar14) goto LAB_020a5f70;
  }
LAB_020a5ed4:
  if (uRam00000000021242ec == uRam00000000021244f8) {
    if (uRam00000000021244f8 != 0) {
      pcVar12 = (char *)0x21240ec;
      do {
        if (*pcVar12 != *pcVar9) goto LAB_020a5f78;
        uVar6 = uVar6 - 1;
        pcVar9 = pcVar9 + 1;
        pcVar12 = pcVar12 + 1;
      } while (uVar6 != 0);
    }
    unaff_x30 = 0x20a5f18;
    register0x00000008 = (BADSPACEBASE *)auStack_50;
    unaff_x19 = uVar7;
    unaff_x20 = 0x21244f4;
    unaff_x29 = puVar1;
code_r0x020a714c:
    *(undefined1 **)((long)register0x00000008 + -0x20) = unaff_x29;
    *(undefined8 *)((long)register0x00000008 + -0x18) = unaff_x30;
    *(undefined8 *)((long)register0x00000008 + -0x10) = unaff_x20;
    *(ulong *)((long)register0x00000008 + -8) = unaff_x19;
    uVar5 = FUN_020a7cec();
    FUN_0208ada0(0x20d01dd,uVar5);
    iVar4 = FUN_020a7cec();
    if (iVar4 == 0) {
      iVar4 = FUN_020a0e58(*puRam0000000002103820,0x21240ec,uRam00000000021242ec);
      uVar2 = 0;
      if (iVar4 != 0) {
        uVar2 = 0x101017;
      }
      uVar7 = (ulong)uVar2;
    }
    else {
      FUN_0208ada0(0x20cefb0);
      puVar3 = puRam0000000002103810;
      uVar7 = FUN_0209c58c(0x21244f4,uVar7,0x2123a5c,*puRam0000000002103810);
      if ((int)uVar7 == 0) {
        if (*puVar3 - 1 < 2) {
          uVar7 = func_0x020a7d10(0x2123a9c,0x30,*puRam0000000002103830);
          if ((int)uVar7 != 0) {
            return uVar7;
          }
          uVar8 = 0x30;
        }
        else {
          if (*puVar3 != 0) {
            return 0x10101b;
          }
          uVar7 = func_0x020a7d10(0x2123a9c,0x20,*puRam0000000002103830);
          if ((int)uVar7 != 0) {
            return uVar7;
          }
          uVar8 = 0x20;
        }
        iVar4 = FUN_020a0e58(0x2123a5c,0x2123a9c,uVar8);
        uVar2 = 0;
        if (iVar4 != 0) {
          uVar2 = 0x101017;
        }
        uVar7 = (ulong)uVar2;
      }
    }
    return uVar7;
  }
LAB_020a5f68:
  uVar6 = 0x101015;
LAB_020a5cb0:
  iRam0000000002123ae8 = 0;
  uRam0000000002123ae0 = 0;
  if (*plRam0000000002103280 == lStack_38) {
    return uVar6;
  }
LAB_020a5f80:
  iRam0000000002123ae8 = 0;
  uRam0000000002123ae0 = 0;
  uVar5 = FUN_02091090(uVar6);
  iVar4 = func_0x0209c6dc();
  if (iVar4 == 0) {
    uVar7 = 0x10101a;
  }
  else {
    FUN_02091664(0x2123858,0,0x200);
    func_0x020a0dfc(0x2123858,0x21240ec,uVar5);
    uRam0000000002123a58 = uVar5;
    func_0x020a0fac(0x2123858,uVar5);
    uVar7 = 0;
  }
  return uVar7;
}


