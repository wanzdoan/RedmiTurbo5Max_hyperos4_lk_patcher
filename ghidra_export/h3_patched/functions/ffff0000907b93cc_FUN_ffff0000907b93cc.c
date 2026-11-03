// ffff0000907b93cc  size=328  FUN_ffff0000907b93cc

undefined8 FUN_ffff0000907b93cc(long param_1,long param_2)

{
  undefined8 uVar1;
  ulong uVar2;
  long lVar3;
  long lVar4;
  long *plVar5;
  long lVar6;
  ulong uVar7;
  ulong local_140 [32];
  
  uVar1 = 0x7247;
  if ((param_1 != 0) && (param_2 != 0)) {
    local_140[0x1e] = 0;
    local_140[0x1f] = 0;
    local_140[0x1c] = 0;
    local_140[0x1d] = 0;
    uVar7 = *(ulong *)(param_1 + 0xb8) & 0x7f;
    local_140[0x1a] = 0;
    local_140[0x1b] = 0;
    local_140[0x18] = 0;
    local_140[0x19] = 0;
    local_140[0x16] = 0;
    local_140[0x17] = 0;
    local_140[0x14] = 0;
    local_140[0x15] = 0;
    local_140[0x12] = 0;
    local_140[0x13] = 0;
    local_140[0x10] = 0;
    local_140[0x11] = 0;
    local_140[0xe] = 0;
    local_140[0xf] = 0;
    local_140[0xc] = 0;
    local_140[0xd] = 0;
    local_140[10] = 0;
    local_140[0xb] = 0;
    local_140[8] = 0;
    local_140[9] = 0;
    local_140[6] = 0;
    local_140[7] = 0;
    local_140[4] = 0;
    local_140[5] = 0;
    local_140[2] = 0;
    local_140[3] = 0;
    local_140[0] = 0;
    local_140[1] = 0;
    FUN_ffff00009077e858(local_140,param_1,uVar7);
    lVar3 = 0x78;
    if (0x6f < uVar7) {
      lVar3 = 0xf8;
    }
    uVar2 = *(long *)(param_1 + 0xb8) << 3;
    uVar1 = 0x80;
    if (0x6f < uVar7) {
      uVar1 = 0x100;
    }
    uVar2 = (uVar2 & 0xff00ff00ff00ff00) >> 8 | (uVar2 & 0xff00ff00ff00ff) << 8;
    uVar2 = (uVar2 & 0xffff0000ffff0000) >> 0x10 | (uVar2 & 0xffff0000ffff) << 0x10;
    *(undefined1 *)((long)local_140 + uVar7) = 0x80;
    *(ulong *)((long)local_140 + lVar3) = uVar2 >> 0x20 | uVar2 << 0x20;
    uVar1 = FUN_ffff0000907b9200(param_1,local_140,uVar1);
    if ((int)uVar1 == 0) {
      lVar3 = 0;
      lVar4 = *(long *)(param_1 + 0x80);
      plVar5 = &DAT_ffff000090813320;
      do {
        lVar6 = *plVar5;
        *(uint *)(param_1 + 0xb4 + lVar3) =
             (*(uint *)(lVar6 + lVar4) & 0xff00) << 8 | *(int *)(lVar6 + lVar4) << 0x18 |
             *(uint *)(lVar6 + lVar4) >> 8 & 0xff00 | *(uint *)(lVar6 + lVar4) >> 0x18;
        lVar3 = lVar3 + -4;
        plVar5 = plVar5 + 1;
      } while (lVar3 != -0x30);
      FUN_ffff00009077e858(param_2,param_1 + 0x88,0x30);
      uVar1 = 0;
    }
  }
  return uVar1;
}


