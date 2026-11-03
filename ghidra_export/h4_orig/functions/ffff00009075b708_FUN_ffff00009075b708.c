// ffff00009075b708  size=948  FUN_ffff00009075b708

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075b708(undefined8 param_1,long param_2)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  uint *puVar4;
  long lVar5;
  uint uVar6;
  long lVar7;
  
  if (*(int *)(param_2 + 0xc) != 0) {
    _DAT_ffff000032000030 = 4;
    if (_DAT_ffff00009087b6f4 != 0) {
      _DAT_ffff000032000030 = 2;
    }
    _DAT_ffff000032000030 = _DAT_ffff000032000030 | _DAT_ffff00009087b6f4 == 0;
    uVar6 = *(uint *)(param_2 + 0x928);
    uVar2 = *(uint *)(param_2 + 0x92c);
    _DAT_ffff00003252001c =
         _DAT_ffff00003252001c & 0xe0000000 |
         _DAT_ffff00003252001c & 0xe000 | *(uint *)(param_2 + 0x92c) & 0x1fff |
         (*(uint *)(param_2 + 0x928) & 0x1fff) << 0x10;
    _DAT_ffff000032520000 = 1;
    _DAT_ffff000032520014 = _DAT_ffff000032520014 | 2;
    if (((*(int *)(param_2 + 0x3d8) == 0) || (*(int *)(param_2 + 0x4fc) == 1)) ||
       (*(int *)(param_2 + 0x508) == 0)) {
      _DAT_ffff000032520018 = _DAT_ffff000032520018 | 0x30000;
    }
    else {
      _DAT_ffff000032520018 =
           _DAT_ffff000032520018 & 0xfffee000 | 0x20000 |
           _DAT_ffff000032520018 & 0xe00 |
           _DAT_ffff000032520018 & 0xc0 |
           *(int *)(param_2 + 0x508) << (ulong)(*(uint *)(param_2 + 0x52c) & 0x1f) & 0x3fU |
           (*(uint *)(param_2 + 0x520) & 1) << 8 | (*(uint *)(param_2 + 0x51c) & 1) << 0xc;
      if (*(uint *)(param_2 + 0x52c) < 6) {
        _DAT_ffff000032520020 =
             *(undefined4 *)(&DAT_ffff00009080e480 + (long)(int)*(uint *)(param_2 + 0x52c) * 4);
      }
      else {
        _DAT_ffff000032520020 = 0x3154203;
      }
    }
    uVar1 = (ulong)(_DAT_ffff00009087b6f4 == 0) << 0x10;
    if (_DAT_ffff00009087b6f4 == 0) {
      uRamffff0000321d0004 = 0;
      uRamffff0000321d0008 = 0;
      uRamffff0000321d0040 = 0;
      uRamffff0000321d0044 = 0;
      uRamffff0000321d0048 = 0;
      puVar4 = (uint *)0xffff0000321d0054;
      uRamffff0000321d000c = uVar6;
      uRamffff0000321d0010 = uVar2;
      uRamffff0000321d0014 = uVar6;
      uRamffff0000321d0018 = uVar2;
      uRamffff0000321d004c = uVar6;
      uRamffff0000321d0050 = uVar2;
      if ((*(int *)(param_2 + 0x3d8) == 1) && (*(int *)(param_2 + 0x4fc) == 0)) {
        lVar5 = 0;
        uRamffff0000321d0054 = uRamffff0000321d0054 & 0xffffffdf | 0xd;
        lVar7 = *(long *)(param_2 + 0x740);
        do {
          *(undefined4 *)(lVar5 + -0xffffcde2ff80) = *(undefined4 *)(lVar7 + lVar5);
          lVar5 = lVar5 + 4;
        } while (lVar5 != 0xc0);
        return 0;
      }
      uVar6 = 0x20;
      uVar2 = 1;
      uRamffff0000321d0054 = uRamffff0000321d0054 | 4;
    }
    else {
      puVar4 = (uint *)(uVar1 | 0xffff0000321c0040);
      *puVar4 = *puVar4 & 0xffffe000 | uVar6 & 0x1fff;
      *puVar4 = *puVar4 & 0xe0000000 | *puVar4 & 0xffff | (uVar2 & 0x1fff) << 0x10;
      puVar4 = (uint *)(uVar1 | 0xffff0000321c0030);
      *(uint *)(uVar1 | 0xffff0000321c0044) = *(uint *)(uVar1 | 0xffff0000321c0044) & 0xff00ffff;
      *puVar4 = *puVar4 & 0xffffe000 | uVar6 & 0x1fff;
      *puVar4 = *puVar4 & 0xe0000000 | *puVar4 & 0xffff | (uVar2 & 0x1fff) << 0x10;
      if ((*(int *)(param_2 + 0x3d8) == 0) || (*(int *)(param_2 + 0x4fc) == 1)) {
        uVar3 = *(uint *)(uVar1 | 0xffff0000321c000c) | 1;
        uVar6 = 0x10;
      }
      else {
        lVar5 = 0;
        lVar7 = *(long *)(param_2 + 0x738);
        do {
          *(undefined4 *)((long)(uVar1 | 0xffff0000321c0080) + lVar5) =
               *(undefined4 *)(lVar7 + lVar5);
          lVar5 = lVar5 + 4;
        } while (lVar5 != 0x1ec);
        *(uint *)(uVar1 | 0xffff0000321c0080) = uVar6 + uVar2 * 0x1000;
        uVar6 = 0x21;
        if (*(int *)(param_2 + 0x504) == 0) {
          uVar6 = 1;
        }
        *(uint *)(uVar1 | 0xffff0000321c0010) =
             uVar6 | (uint)(*(int *)(param_2 + 0x500) != 0) << 4 |
             (uint)(*(int *)(param_2 + 0x518) != 0) << 2 |
             (uint)(*(int *)(param_2 + 0x514) != 0) << 1;
        if (*(int *)(param_2 + 0x504) == 0) {
          puVar4 = (uint *)(uVar1 | 0xffff0000321c0d90);
          *puVar4 = *puVar4 | 2;
          *puVar4 = *puVar4 | 4;
        }
        puVar4 = (uint *)(uVar1 | 0xffff0000321c000c);
        *puVar4 = *puVar4 | 0x400000;
        *puVar4 = *puVar4 | 0x200000;
        uVar3 = *puVar4 & 0xffffffef;
        uVar6 = 1;
      }
      puVar4 = (uint *)(uVar1 | 0xffff0000321c000c);
      uVar2 = 2;
      *puVar4 = uVar3;
    }
    *puVar4 = *puVar4 | uVar2;
    *puVar4 = *puVar4 | uVar6;
  }
  return 0;
}


