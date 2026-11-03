// ffff000090754360  size=412  FUN_ffff000090754360

void FUN_ffff000090754360(int param_1)

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
  
  FUN_ffff00009077eb88(auStack_268,0,0x200);
  puVar1 = PTR_DAT_ffff000090839980;
  uVar7 = 0;
  do {
    lVar10 = uVar7 * 0x20;
    if (param_1 == 0) {
      lVar6 = *(long *)PTR_DAT_ffff000090839988;
    }
    else {
      lVar6 = *(long *)puVar1;
    }
    if (*(int *)(lVar6 + lVar10 + 0x34) != 0) {
      uVar2 = *(uint *)(lVar6 + lVar10 + 0x30);
      uVar3 = FUN_ffff000090758df0(uVar2 & 0xf);
      uVar4 = FUN_ffff000090758df0(uVar2 >> 7 & 0xf);
      uVar2 = FUN_ffff00009077f38c
                        (auStack_268,s_MUTEX_d__SOF__s_EOF__s_WAIT__d_m_ffff0000907e4f33,
                         uVar7 & 0xffffffff,uVar3,uVar4,uVar2 >> 6 & 1);
      lVar6 = 0;
      puVar8 = auStack_268 + (uVar2 & ((int)uVar2 >> 0x1f ^ 0xffffffffU));
      do {
        uVar9 = (uint)lVar6;
        uVar2 = 1 << (ulong)(uVar9 & 0x1f);
        if (param_1 == 0) {
          if ((*(uint *)(*(long *)PTR_DAT_ffff000090839988 + lVar10 + 0x34) & uVar2) != 0) {
            pcVar5 = s_unknown_mutex_ffff0000907e1a1b;
            if (uVar9 < 0x20) {
              pcVar5 = (char *)((long)*(int *)(lVar6 * 4 + -0xffff6f7f3044) + -0xffff6f7f3044);
            }
            goto LAB_ffff0000907544c0;
          }
        }
        else if ((*(uint *)(*(long *)puVar1 + lVar10 + 0x34) & uVar2) != 0) {
          pcVar5 = s_unknown_mutex_ffff0000907e1a1b;
          if (uVar9 < 0x1d) {
            pcVar5 = (char *)((long)&DAT_ffff00009080d03c +
                             (long)(int)(&DAT_ffff00009080d03c)[lVar6]);
          }
LAB_ffff0000907544c0:
          uVar2 = FUN_ffff00009077f38c(puVar8,&DAT_ffff0000907e9382,pcVar5);
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


