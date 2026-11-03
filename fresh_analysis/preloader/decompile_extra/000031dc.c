// File offset entry: 0x31dc

ulong FUN_020030dc(ulong param_1,ulong param_2,ulong param_3,ulong param_4,ulong param_5,
                  long param_6)

{
  long lVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  long lVar5;
  ulong uVar6;
  undefined8 uVar7;
  ulong uVar8;
  uint uVar9;
  undefined8 uVar10;
  undefined8 uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong uVar14;
  ulong uVar15;
  ulong uVar16;
  ulong uVar17;
  ulong uVar18;
  ulong uVar19;
  undefined1 auVar20 [16];
  
  uVar4 = (int)param_5 - 3;
  uVar3 = 1 << (ulong)(uVar4 & 0x1f);
  uVar15 = 1L << (param_4 & 0x3f);
  uVar18 = (ulong)(uVar3 & ((int)uVar3 >> 0x1f ^ 0xffffffffU));
  uVar6 = param_1;
  do {
    if (param_3 == 0) {
      return uVar6;
    }
    uVar17 = param_1 >> 0xc & 0xfffffffffff;
    uVar16 = param_2 >> (param_4 & 0x3f);
    uVar19 = param_2 & uVar15 - 1;
    uVar14 = uVar15 - uVar19;
    uVar12 = *(ulong *)(param_6 + uVar16 * 8);
    uVar2 = param_3;
    if (uVar14 <= param_3) {
      uVar2 = uVar14;
    }
    uVar9 = (uint)param_5;
    if (uVar9 < (uint)param_4 && (uVar12 & 3) == 3) {
      param_5 = param_5 & 0xffffffff;
      lVar5 = FUN_0206ee94();
      uVar8 = (ulong)((uint)param_4 - uVar4);
      uVar14 = uVar2;
      uVar6 = FUN_020030dc(param_1,uVar19,uVar2,uVar8,param_5,lVar5);
      if (uVar2 != uVar15) {
        uVar19 = 0;
        do {
          uVar13 = uVar18;
          if (uVar18 == uVar19) break;
          lVar1 = uVar19 * 8;
          uVar13 = uVar19;
          uVar19 = uVar19 + 1;
        } while (*(long *)(lVar5 + lVar1) == 0);
        if ((long)uVar13 < (long)(int)uVar3) goto LAB_02003258;
      }
      *(undefined8 *)(param_6 + uVar16 * 8) = 0;
      DataMemoryBarrier(2,2);
      TLBI_VAE3IS(uVar17);
      InstructionSynchronizationBarrier();
      if (uVar9 < 0xc) {
        uVar6 = FUN_02002bf8(lVar5);
      }
      else {
        uVar6 = param_5;
        lVar5 = func_0x0208a558(uVar12 & 0xfffffffff000);
        if (lVar5 == 0) {
          auVar20 = FUN_0208b29c(0x20bc243,uVar12 & 0xfffffffff000);
          uVar15 = auVar20._8_8_;
          lVar5 = auVar20._0_8_;
          if ((uVar15 < *(ulong *)(lVar5 + 0x18)) ||
             ((*(ulong *)(lVar5 + 0x18) + *(long *)(lVar5 + 0x20)) - 1 < uVar15)) {
            uVar6 = 0xffffffdb;
          }
          else if ((((uint)uVar14 | auVar20._8_4_) & 0xfff) == 0) {
            if ((int)uVar8 == 0) {
              uVar6 = 0;
            }
            else {
              uVar3 = *(uint *)(lVar5 + 0x10);
              uVar7 = FUN_02003374(uVar6 & 0xffffffff);
              if ((uVar3 & 1) == 0) {
                uVar10 = 0;
                uVar11 = 0x27;
              }
              else {
                uVar10 = 0xffffffff80000000;
                uVar11 = 0x1f;
              }
              uVar6 = FUN_02002c7c(uVar15,uVar14,(uVar8 & 0xffffffff) << 0xc,uVar7,uVar10,uVar11,
                                   0x1e,0xc,*(undefined8 *)(lVar5 + 8));
            }
          }
          else {
            uVar6 = 0xfffffff8;
          }
          return uVar6;
        }
        uVar6 = func_0x0208a930();
      }
    }
    else if (uVar12 != 0) {
      *(undefined8 *)(param_6 + uVar16 * 8) = 0;
      DataMemoryBarrier(2,2);
      TLBI_VAE3IS(uVar17);
      InstructionSynchronizationBarrier();
    }
LAB_02003258:
    param_1 = uVar2 + param_1;
    param_2 = uVar2 + param_2;
    param_3 = param_3 - uVar2;
  } while( true );
}


