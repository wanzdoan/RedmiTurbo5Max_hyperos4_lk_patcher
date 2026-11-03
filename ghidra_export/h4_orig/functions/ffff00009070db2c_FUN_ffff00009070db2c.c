// ffff00009070db2c  size=704  FUN_ffff00009070db2c

undefined4 FUN_ffff00009070db2c(char *param_1)

{
  long lVar1;
  int iVar2;
  undefined4 uVar3;
  long lVar4;
  undefined8 uVar5;
  long lVar6;
  char *pcVar7;
  char *pcVar9;
  ulong uVar10;
  long lVar11;
  ulong uVar12;
  undefined1 *puVar13;
  long local_338 [91];
  char *pcVar8;
  
  FUN_ffff00009077ee50(local_338,0,0x2d0);
  lVar11 = 0;
  do {
    lVar4 = FUN_ffff00009077d514(0x14);
    *(long *)((long)local_338 + lVar11) = lVar4;
    if (lVar4 != 0) {
      FUN_ffff00009077ee50(lVar4,0,0x14);
    }
    lVar11 = lVar11 + 8;
  } while (lVar11 != 0x2d0);
  if (param_1 != (char *)0x0) {
    do {
      pcVar9 = param_1;
      param_1 = pcVar9 + 1;
    } while (*pcVar9 == '\n');
    pcVar7 = pcVar9 + 1;
    if (*pcVar9 != '\0') {
      do {
        pcVar8 = pcVar7;
        pcVar7 = pcVar8 + 1;
        if (*pcVar8 == '\0') {
          pcVar7 = (char *)0x0;
          goto LAB_ffff00009070dbb8;
        }
      } while (*pcVar8 != '\n');
      *pcVar8 = '\0';
      goto LAB_ffff00009070dbb8;
    }
  }
  pcVar7 = (char *)0x0;
  pcVar9 = (char *)0x0;
LAB_ffff00009070dbb8:
  uVar5 = FUN_ffff00009077f250(s_SPARSECRC_LIST_ffff0000907e8d10);
  iVar2 = FUN_ffff00009077eae8(pcVar9,s_SPARSECRC_LIST_ffff0000907e8d10,uVar5);
  if (iVar2 == 0) {
    lVar11 = FUN_ffff00009077f074(pcVar7,10);
    if (lVar11 != 0) {
      uVar10 = 0;
      puVar13 = &DAT_ffff00009083d6dc;
      do {
        if (uVar10 == 4) goto LAB_ffff00009070dc40;
        pcVar7[(int)lVar11 - (int)pcVar7] = '\0';
        iVar2 = FUN_ffff00009070d8fc(pcVar7,local_338);
        lVar4 = local_338[0];
        if (iVar2 != 0) goto LAB_ffff00009070dc40;
        lVar1 = uVar10 * 0x17c;
        lVar6 = FUN_ffff00009077f250(local_338[0]);
        FUN_ffff00009077f2ac(lVar1 + -0xffff6f7c2938,lVar4,lVar6 + 1);
        uVar3 = FUN_ffff00009077d938(local_338[1],0,10);
        *(undefined4 *)(&DAT_ffff00009083d83c + lVar1) = uVar3;
        FUN_ffff00009077e11c
                  (s_sparsecrc_list__d__partition_nam_ffff0000907f26a0,uVar10 & 0xffffffff,
                   lVar1 + -0xffff6f7c2938);
        if (*(int *)(&DAT_ffff00009083d83c + lVar1) != 0) {
          uVar12 = 0;
          do {
            uVar3 = FUN_ffff00009077d938(local_338[(int)uVar12 + 2],0,0x10);
            *(undefined4 *)(puVar13 + uVar12 * 4) = uVar3;
            FUN_ffff00009077e11c
                      (s_sparsecrc_crcvalue__d___0x_x_ffff0000907cf154,uVar12 & 0xffffffff);
            uVar12 = uVar12 + 1;
          } while (uVar12 < *(uint *)(&DAT_ffff00009083d83c + lVar1));
        }
        uVar10 = uVar10 + 1;
        pcVar7 = (char *)(lVar11 + 1);
        *(undefined4 *)(uVar10 * 0x17c + -0xffff6f7c27c0) = 0;
        lVar11 = FUN_ffff00009077f074(pcVar7,10);
        puVar13 = puVar13 + 0x17c;
      } while (lVar11 != 0);
    }
    uVar3 = 0;
    DAT_ffff00009083dcb8 = 1;
  }
  else {
    lVar11 = 0x14;
    do {
      *(undefined4 *)(lVar11 + -0xffff6f7c2938) = 0;
      lVar11 = lVar11 + 4;
    } while (lVar11 != 0x174);
    lVar11 = 0;
    do {
      lVar4 = lVar11 + 4;
      *(undefined4 *)(lVar11 + -0xffff6f7c27a8) = 0;
      lVar11 = lVar4;
    } while (lVar4 != 0x160);
    lVar11 = 0;
    do {
      lVar4 = lVar11 + 4;
      *(undefined4 *)(lVar11 + -0xffff6f7c262c) = 0;
      lVar11 = lVar4;
    } while (lVar4 != 0x160);
    lVar11 = 0;
    do {
      lVar4 = lVar11 + 4;
      *(undefined4 *)(lVar11 + -0xffff6f7c24b0) = 0;
      lVar11 = lVar4;
    } while (lVar4 != 0x160);
LAB_ffff00009070dc40:
    DAT_ffff00009083dcb8 = 0;
    uVar3 = 1;
  }
  lVar11 = 0;
  do {
    if (*(long *)((long)local_338 + lVar11) != 0) {
      thunk_FUN_ffff0000907c314c();
    }
    lVar11 = lVar11 + 8;
  } while (lVar11 != 0x2d0);
  return uVar3;
}


