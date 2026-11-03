
void FUN_ffff0000be03f9c8(int param_1)

{
  long lVar1;
  undefined *puVar2;
  undefined *puVar3;
  uint uVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  char *pcVar7;
  long lVar8;
  long lVar9;
  long lVar10;
  ulong uVar11;
  long lVar12;
  undefined1 *puVar13;
  undefined1 auStack_268 [520];
  
  FUN_ffff0000be060ae0(auStack_268,0,0x200);
  puVar3 = PTR_DAT_ffff0000be0f5650;
  puVar2 = PTR_DAT_ffff0000be0f5648;
  uVar11 = 0;
  do {
    lVar12 = uVar11 * 0x20;
    if (param_1 == 0) {
      lVar8 = *(long *)puVar2;
    }
    else {
      lVar8 = *(long *)puVar3;
    }
    if (*(int *)(lVar8 + lVar12 + 0x34) != 0) {
      uVar4 = *(uint *)(lVar8 + lVar12 + 0x30);
      uVar5 = FUN_ffff0000be044064(uVar4 & 0xf);
      uVar6 = FUN_ffff0000be044064(uVar4 >> 7 & 0xf);
      uVar4 = FUN_ffff0000be061134
                        (auStack_268,s_MUTEX_d__SOF__s_EOF__s_WAIT__d_m_ffff0000be0b2248,
                         uVar11 & 0xffffffff,uVar5,uVar6,uVar4 >> 6 & 1);
      lVar8 = 0;
      lVar9 = *(long *)puVar2;
      puVar13 = auStack_268 + (uVar4 & ((int)uVar4 >> 0x1f ^ 0xffffffffU));
      lVar10 = *(long *)puVar3;
      do {
        lVar1 = lVar9;
        if (param_1 != 0) {
          lVar1 = lVar10;
        }
        if ((*(uint *)(lVar12 + lVar1 + 0x34) >> (ulong)((uint)lVar8 & 0x1f) & 1) != 0) {
          pcVar7 = s_unknown_mutex_ffff0000be0b0001;
          if ((uint)lVar8 < 0x20) {
            pcVar7 = (char *)((long)&DAT_ffff0000be0cb210 +
                             (long)(int)(&DAT_ffff0000be0cb210)[lVar8]);
          }
          uVar4 = FUN_ffff0000be061134(puVar13,&DAT_ffff0000be0b52b6,pcVar7);
          lVar9 = *(long *)puVar2;
          lVar10 = *(long *)puVar3;
          if (-1 < (int)uVar4) {
            puVar13 = puVar13 + uVar4;
          }
        }
        lVar8 = lVar8 + 1;
      } while ((int)lVar8 != 0x20);
      lVar8 = 0;
      do {
        lVar1 = lVar9;
        if (param_1 != 0) {
          lVar1 = lVar10;
        }
        if ((*(uint *)(lVar12 + lVar1 + 0x38) >> (ulong)((uint)lVar8 & 0x1f) & 1) != 0) {
          pcVar7 = s_unknown_mutex_ffff0000be0b0001;
          if ((uint)lVar8 < 0x1a) {
            pcVar7 = (char *)((long)&DAT_ffff0000be0cb290 +
                             (long)(int)(&DAT_ffff0000be0cb290)[lVar8]);
          }
          uVar4 = FUN_ffff0000be061134(puVar13,&DAT_ffff0000be0b52b6,pcVar7);
          lVar9 = *(long *)puVar2;
          lVar10 = *(long *)puVar3;
          if (-1 < (int)uVar4) {
            puVar13 = puVar13 + uVar4;
          }
        }
        lVar8 = lVar8 + 1;
      } while ((int)lVar8 != 0x20);
    }
    uVar11 = uVar11 + 1;
  } while (uVar11 != 5);
  return;
}


