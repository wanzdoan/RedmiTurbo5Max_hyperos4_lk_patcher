// File offset entry: 0x4bac

/* WARNING: Possible PIC construction at 0x0206e2b4: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0206e56c: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0206e884: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0206e570) */
/* WARNING: Removing unreachable block (ram,0x0206e590) */
/* WARNING: Removing unreachable block (ram,0x0206e574) */
/* WARNING: Removing unreachable block (ram,0x0206e2b8) */
/* WARNING: Removing unreachable block (ram,0x0206e2bc) */
/* WARNING: Removing unreachable block (ram,0x0206e2c4) */
/* WARNING: Removing unreachable block (ram,0x0206e2e8) */
/* WARNING: Removing unreachable block (ram,0x0206e45c) */
/* WARNING: Removing unreachable block (ram,0x0206e4d4) */
/* WARNING: Removing unreachable block (ram,0x0206e4dc) */
/* WARNING: Removing unreachable block (ram,0x0206e514) */
/* WARNING: Removing unreachable block (ram,0x0206e4f0) */
/* WARNING: Removing unreachable block (ram,0x0206e530) */
/* WARNING: Removing unreachable block (ram,0x0206e4f8) */
/* WARNING: Removing unreachable block (ram,0x0206e54c) */
/* WARNING: Removing unreachable block (ram,0x0206e500) */
/* WARNING: Removing unreachable block (ram,0x0206e504) */
/* WARNING: Removing unreachable block (ram,0x0206e510) */
/* WARNING: Removing unreachable block (ram,0x0206e568) */
/* WARNING: Removing unreachable block (ram,0x0206e56c) */
/* WARNING: Removing unreachable block (ram,0x0206e2d0) */
/* WARNING: Removing unreachable block (ram,0x0206e888) */
/* WARNING: Removing unreachable block (ram,0x0206e8a8) */
/* WARNING: Removing unreachable block (ram,0x0206e88c) */

long * FUN_02004aac(uint param_1)

{
  byte bVar1;
  int iVar2;
  undefined1 uVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  long *plVar7;
  long *plVar8;
  long *plVar9;
  undefined4 uVar10;
  uint uVar11;
  undefined1 auVar12 [16];
  undefined1 auStack_290 [512];
  undefined4 uStack_90;
  undefined4 uStack_8c;
  undefined4 uStack_88;
  undefined4 uStack_84;
  undefined4 uStack_80;
  undefined4 uStack_7c;
  undefined4 uStack_78;
  undefined4 uStack_74;
  undefined4 uStack_70;
  undefined4 uStack_6c;
  undefined1 *puStack_60;
  undefined8 uStack_58;
  undefined1 auStack_30 [16];
  
  iVar4 = func_0x020049c4();
  if (iVar4 == 2) {
    lVar5 = func_0x02095114(0);
    uVar3 = param_1 == 1;
    (**(code **)(lVar5 + 0x248))(lVar5,4,0,0,0,&stack0xffffffffffffffe4);
    plVar8 = (long *)func_0x020961c0();
    if (!(bool)uVar3) {
      auVar12 = FUN_02091090();
                    /* WARNING: Could not recover jumptable at 0x02094f10. Too many branches */
                    /* WARNING: Treating indirect jump as call */
      plVar8 = (long *)(**(code **)(auVar12._0_8_ + 0x248))(auVar12._0_8_,3,0,0,0,auVar12._8_8_);
      return plVar8;
    }
    return plVar8;
  }
  if (iVar4 != 1) {
    return (long *)0xffffffff;
  }
  if (param_1 < 3) {
    lVar5 = func_0x0206e338(1);
    lVar6 = func_0x0206e31c(1);
    if ((lVar5 != 0) && (lVar6 != 0)) {
      uStack_58 = 0x206e2b8;
      puStack_60 = auStack_30;
      auVar12 = func_0x0206e8cc(lVar6,lVar5);
      plVar9 = auVar12._8_8_;
      plVar8 = auVar12._0_8_;
      if (3 < *(byte *)((long)plVar9 + 0x26d)) {
        uStack_70 = 0;
        uStack_6c = 0;
        uStack_78 = 0;
        uStack_74 = 0;
        uStack_80 = 0;
        uStack_7c = 0;
        uStack_84 = 0;
        FUN_02091664(auStack_290,0,0x200);
        FUN_02091664(auStack_290,0,0x200);
        uStack_90 = 8;
        uStack_8c = 0;
        uStack_88 = 1;
        uStack_74 = 100;
        uStack_70 = 5;
        func_0x0207015c(plVar8);
        do {
          func_0x0206f3cc(plVar8,1);
          func_0x0206f3b4(plVar8,0x200);
          func_0x0206f2cc(plVar8,100000000,0);
          plVar7 = (long *)func_0x0206e354(plVar8,&uStack_90);
          if ((int)plVar7 != 0) {
            return plVar7;
          }
          iVar4 = func_0x0206ff58(plVar8,auStack_290,0x200);
          if (iVar4 == 0) {
            func_0x0207015c(plVar8);
            plVar8 = (long *)FUN_020915ac(plVar9 + 10,auStack_290,0x200);
            *(uint *)(plVar9 + 0x54) = (uint)*(byte *)(plVar9 + 0x26) << 0x13;
            *(uint *)((long)plVar9 + 0x29c) = (uint)*(byte *)((long)plVar9 + 0x12d);
            iVar4 = (uint)*(byte *)(plVar9 + 0x26) * (uint)*(byte *)((long)plVar9 + 0x12d) * 0x200;
            *(uint *)((long)plVar9 + 0x2ac) = (uint)*(byte *)(plVar9 + 0x1f) << 0x11;
            *(char *)(plVar9 + 0x53) = (char)plVar9[0x22];
            *(uint *)(plVar9 + 0x56) = (uint)*(byte *)((long)plVar9 + 0x132) << 0x11;
            *(uint *)((long)plVar9 + 0x2b4) =
                 (uint)CONCAT12(*(undefined1 *)((long)plVar9 + 0xe1),
                                *(undefined2 *)((long)plVar9 + 0xdf)) * iVar4;
            iVar2 = CONCAT13(*(undefined1 *)((long)plVar9 + 0x127),
                             *(undefined3 *)((long)plVar9 + 0x124));
            *(uint *)(plVar9 + 0x57) =
                 (uint)CONCAT12(*(undefined1 *)((long)plVar9 + 0xe4),
                                *(undefined2 *)((long)plVar9 + 0xe2)) * iVar4;
            *(int *)((long)plVar9 + 0x2a4) = iVar2;
            *(uint *)((long)plVar9 + 700) =
                 (uint)CONCAT12(*(undefined1 *)((long)plVar9 + 0xe7),
                                CONCAT11(*(undefined1 *)((long)plVar9 + 0xe6),
                                         *(undefined1 *)((long)plVar9 + 0xe5))) * iVar4;
            *(uint *)(plVar9 + 0x58) =
                 (uint)CONCAT12(*(undefined1 *)((long)plVar9 + 0xea),
                                CONCAT11(*(undefined1 *)((long)plVar9 + 0xe9),(char)plVar9[0x1d])) *
                 iVar4;
            if (iVar2 != 0) {
              *(ushort *)((long)plVar9 + 0x24) = *(ushort *)((long)plVar9 + 0x24) | 8;
            }
            bVar1 = *(byte *)((long)plVar9 + 0x114);
            *(undefined4 *)(plVar9 + 0x55) = 0;
            if ((bVar1 & 0x30) == 0) {
              if ((bVar1 & 0xc) == 0) {
                if ((bVar1 >> 1 & 1) == 0) {
                  if ((bVar1 & 1) == 0) {
                    plVar8 = (long *)FUN_0208ada0(0x20b79de,*(undefined1 *)(*plVar9 + 0x5d));
                    goto LAB_0206e844;
                  }
                  uVar3 = 0x2a;
                  uVar10 = 26000000;
                }
                else {
                  uVar3 = 0x2b;
                  uVar10 = 52000000;
                }
                *(undefined4 *)(plVar9 + 0x55) = uVar10;
              }
              else {
                *(undefined4 *)(plVar9 + 0x55) = 52000000;
                uVar3 = 0x2c;
                *(byte *)((long)plVar9 + 0x2c4) = *(byte *)((long)plVar9 + 0x2c4) | 4;
              }
              *(undefined1 *)(plVar9 + 0x5c) = uVar3;
            }
            else {
              *(undefined4 *)(plVar9 + 0x55) = 200000000;
              if ((bVar1 & 0xc) != 0) {
                *(byte *)((long)plVar9 + 0x2c4) = *(byte *)((long)plVar9 + 0x2c4) | 4;
              }
              *(undefined1 *)(plVar9 + 0x5c) = 0x2d;
            }
LAB_0206e844:
            *(undefined1 *)((long)plVar9 + 0x2c5) = *(undefined1 *)((long)plVar9 + 0xfb);
            *(undefined1 *)((long)plVar9 + 0x2c6) = *(undefined1 *)((long)plVar9 + 0xfd);
            if ((*(byte *)((long)plVar9 + 0xff) & 1) == 0) {
              uVar11 = *(int *)((long)plVar9 + 0x28c) +
                       *(int *)((long)plVar9 + 0x28c) * (int)plVar9[0x52];
            }
            else {
              uVar11 = (uint)((int)plVar9[0x54] * *(int *)((long)plVar9 + 0x29c)) >> 9;
            }
            *(uint *)((long)plVar9 + 0x2e4) = uVar11;
            return plVar8;
          }
          *plVar8 = (long)plVar9;
          func_0x0206f850(plVar8,1);
          iVar4 = func_0x0207016c(plVar8);
        } while (iVar4 != 7);
        plVar8 = (long *)func_0x0207015c(plVar8);
      }
      return plVar8;
    }
  }
  return (long *)0xffffffff;
}


