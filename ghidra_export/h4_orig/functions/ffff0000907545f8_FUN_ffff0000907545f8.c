// ffff0000907545f8  size=412  FUN_ffff0000907545f8

void FUN_ffff0000907545f8(int param_1)

{
  undefined *puVar1;
  uint uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  char *pcVar5;
  long lVar6;
  ulong uVar7;
  undefined1 *puVar8;
  uint uVar9;
  long lVar10;
  undefined1 auStack_268 [520];
  
  FUN_ffff00009077ee50(auStack_268,0,0x200);
  puVar1 = PTR_DAT_ffff0000908399a0;
  uVar7 = 0;
  do {
    lVar10 = uVar7 * 0x20;
    if (param_1 == 0) {
      lVar6 = *(long *)PTR_DAT_ffff0000908399a8;
    }
    else {
      lVar6 = *(long *)puVar1;
    }
    if (*(int *)(lVar6 + lVar10 + 0x34) != 0) {
      uVar2 = *(uint *)(lVar6 + lVar10 + 0x30);
      uVar3 = FUN_ffff000090759088(uVar2 & 0xf);
      uVar4 = FUN_ffff000090759088(uVar2 >> 7 & 0xf);
      uVar2 = FUN_ffff00009077f654
                        (auStack_268,s_MUTEX_d__SOF__s_EOF__s_WAIT__d_m_ffff0000907e5037,
                         uVar7 & 0xffffffff,uVar3,uVar4,uVar2 >> 6 & 1);
      lVar6 = 0;
      puVar8 = auStack_268 + (uVar2 & ((int)uVar2 >> 0x1f ^ 0xffffffffU));
      do {
        uVar9 = (uint)lVar6;
        uVar2 = 1 << (ulong)(uVar9 & 0x1f);
        if (param_1 == 0) {
          if ((*(uint *)(*(long *)PTR_DAT_ffff0000908399a8 + lVar10 + 0x34) & uVar2) != 0) {
            pcVar5 = s_unknown_mutex_ffff0000907e1aec;
            if (uVar9 < 0x20) {
              pcVar5 = (char *)((long)*(int *)(lVar6 * 4 + -0xffff6f7f2e1c) + -0xffff6f7f2e1c);
            }
            goto LAB_ffff000090754758;
          }
        }
        else if ((*(uint *)(*(long *)puVar1 + lVar10 + 0x34) & uVar2) != 0) {
          pcVar5 = s_unknown_mutex_ffff0000907e1aec;
          if (uVar9 < 0x1d) {
            pcVar5 = (char *)((long)&DAT_ffff00009080d264 +
                             (long)(int)(&DAT_ffff00009080d264)[lVar6]);
          }
LAB_ffff000090754758:
          uVar2 = FUN_ffff00009077f654(puVar8,&DAT_ffff0000907e94c6,pcVar5);
          if (-1 < (int)uVar2) {
            puVar8 = puVar8 + uVar2;
          }
        }
        lVar6 = lVar6 + 1;
      } while ((int)lVar6 != 0x20);
    }
    uVar7 = uVar7 + 1;
    if (uVar7 == 5) {
      return;
    }
  } while( true );
}


