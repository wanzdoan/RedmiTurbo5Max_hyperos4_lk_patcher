
undefined4 FUN_ffff00009070db2c(char *param_1)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  undefined8 uVar4;
  long lVar5;
  char *pcVar6;
  char *pcVar8;
  ulong uVar9;
  long lVar10;
  ulong uVar11;
  undefined4 *puVar12;
  long local_338 [91];
  char *pcVar7;
  
  FUN_ffff00009077ee50(local_338,0,0x2d0);
  lVar10 = 0;
  do {
    lVar3 = FUN_ffff00009077d514(0x14);
    *(long *)((long)local_338 + lVar10) = lVar3;
    if (lVar3 != 0) {
      FUN_ffff00009077ee50(lVar3,0,0x14);
    }
    lVar10 = lVar10 + 8;
  } while (lVar10 != 0x2d0);
  if (param_1 != (char *)0x0) {
    do {
      pcVar8 = param_1;
      param_1 = pcVar8 + 1;
    } while (*pcVar8 == '\n');
    pcVar6 = pcVar8 + 1;
    if (*pcVar8 != '\0') {
      do {
        pcVar7 = pcVar6;
        pcVar6 = pcVar7 + 1;
        if (*pcVar7 == '\0') {
          pcVar6 = (char *)0x0;
          goto LAB_ffff00009070dbb8;
        }
      } while (*pcVar7 != '\n');
      *pcVar7 = '\0';
      goto LAB_ffff00009070dbb8;
    }
  }
  pcVar6 = (char *)0x0;
  pcVar8 = (char *)0x0;
LAB_ffff00009070dbb8:
  uVar4 = FUN_ffff00009077f250(s_SPARSECRC_LIST_ffff0000907e8d10);
  iVar1 = FUN_ffff00009077eae8(pcVar8,s_SPARSECRC_LIST_ffff0000907e8d10,uVar4);
  if (iVar1 == 0) {
    lVar10 = FUN_ffff00009077f074(pcVar6,10);
    if (lVar10 != 0) {
      uVar9 = 0;
      puVar12 = &DAT_ffff00009083d6dc;
      do {
        if (uVar9 == 4) goto LAB_ffff00009070dc40;
        pcVar6[(int)lVar10 - (int)pcVar6] = '\0';
        iVar1 = FUN_ffff00009070d8fc(pcVar6,local_338);
        lVar3 = local_338[0];
        if (iVar1 != 0) goto LAB_ffff00009070dc40;
        lVar5 = FUN_ffff00009077f250(local_338[0]);
        FUN_ffff00009077f2ac(&DAT_ffff00009083d6c8 + uVar9 * 0x17c,lVar3,lVar5 + 1);
        uVar2 = FUN_ffff00009077d938(local_338[1],0,10);
        (&DAT_ffff00009083d83c)[uVar9 * 0x5f] = uVar2;
        FUN_ffff00009077e11c
                  (s_sparsecrc_list__d__partition_nam_ffff0000907f26a0,uVar9 & 0xffffffff,
                   &DAT_ffff00009083d6c8 + uVar9 * 0x17c);
        if ((&DAT_ffff00009083d83c)[uVar9 * 0x5f] != 0) {
          uVar11 = 0;
          do {
            uVar2 = FUN_ffff00009077d938(local_338[(int)uVar11 + 2],0,0x10);
            puVar12[uVar11] = uVar2;
            FUN_ffff00009077e11c
                      (s_sparsecrc_crcvalue__d___0x_x_ffff0000907cf154,uVar11 & 0xffffffff);
            uVar11 = uVar11 + 1;
          } while (uVar11 < (uint)(&DAT_ffff00009083d83c)[uVar9 * 0x5f]);
        }
        uVar9 = uVar9 + 1;
        pcVar6 = (char *)(lVar10 + 1);
        *(undefined4 *)(&DAT_ffff00009083d840 + uVar9 * 0x17c) = 0;
        lVar10 = FUN_ffff00009077f074(pcVar6,10);
        puVar12 = puVar12 + 0x5f;
      } while (lVar10 != 0);
    }
    uVar2 = 0;
    DAT_ffff00009083dcb8 = 1;
  }
  else {
    lVar10 = 0x14;
    do {
      *(undefined4 *)(&DAT_ffff00009083d6c8 + lVar10) = 0;
      lVar10 = lVar10 + 4;
    } while (lVar10 != 0x174);
    lVar10 = 0;
    do {
      lVar3 = lVar10 + 4;
      *(undefined4 *)((long)&DAT_ffff00009083d858 + lVar10) = 0;
      lVar10 = lVar3;
    } while (lVar3 != 0x160);
    lVar10 = 0;
    do {
      lVar3 = lVar10 + 4;
      *(undefined4 *)((long)&DAT_ffff00009083d9d4 + lVar10) = 0;
      lVar10 = lVar3;
    } while (lVar3 != 0x160);
    lVar10 = 0;
    do {
      lVar3 = lVar10 + 4;
      *(undefined4 *)((long)&DAT_ffff00009083db50 + lVar10) = 0;
      lVar10 = lVar3;
    } while (lVar3 != 0x160);
LAB_ffff00009070dc40:
    DAT_ffff00009083dcb8 = 0;
    uVar2 = 1;
  }
  lVar10 = 0;
  do {
    if (*(long *)((long)local_338 + lVar10) != 0) {
      thunk_FUN_ffff0000907c314c();
    }
    lVar10 = lVar10 + 8;
  } while (lVar10 != 0x2d0);
  return uVar2;
}


