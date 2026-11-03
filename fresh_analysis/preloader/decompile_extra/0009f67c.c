// File offset entry: 0x9f67c

ulong FUN_0209f57c(int param_1,undefined1 *param_2,undefined8 *param_3,undefined8 *param_4,
                  long param_5,undefined8 *param_6,ulong param_7,undefined8 param_8)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  ulong uVar4;
  int iVar5;
  int iVar6;
  undefined8 *puVar7;
  undefined1 *puVar8;
  undefined8 *puVar9;
  undefined8 *puVar10;
  undefined1 *puVar11;
  int iVar12;
  long lVar13;
  undefined1 *puVar14;
  undefined1 *puVar15;
  int iVar16;
  undefined8 *puVar17;
  char cVar18;
  undefined1 auVar19 [16];
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  long lStack_48;
  
  iVar3 = (int)param_8;
  iVar5 = (int)param_3;
  iVar6 = (int)param_5;
  uVar1 = iVar6 + iVar5;
  uVar2 = 0xffffffff;
  lStack_48 = *plRam0000000002103280;
  puVar7 = param_6;
  if ((((uVar1 < 0x41) && (param_2 != (undefined1 *)0x0)) && (param_4 != (undefined8 *)0x0)) &&
     (param_6 != (undefined8 *)0x0)) {
    if (param_1 == 3) {
      uVar2 = func_0x0209b9f4(param_6,&uStack_a8);
      puVar7 = param_6;
    }
    else {
      puVar17 = (undefined8 *)(param_7 & 0xffffffff);
      uVar2 = 0x10200f;
      iVar16 = (int)param_7;
      if (param_1 == 2) {
        uStack_58 = 0;
        uStack_50 = 0;
        uStack_68 = 0;
        uStack_60 = 0;
        uStack_78 = 0;
        uStack_70 = 0;
        uStack_88 = 0;
        uStack_80 = 0;
        uStack_98 = 0;
        uStack_90 = 0;
        uStack_a8 = 0;
        uStack_a0 = 0;
        if (((param_7 & 0xf) == 0) && (0xffffffbf < uVar1 - 0x3d)) {
          puVar8 = (undefined1 *)((ulong)&uStack_a8 | 1);
          uStack_a8 = 1;
          iVar3 = (int)puVar8;
          if (0 < iVar5) {
            puVar11 = puVar8;
            puVar15 = puVar8;
            do {
              puVar11 = puVar11 + 1;
              iVar3 = (int)puVar11;
              puVar14 = puVar15 + 1;
              *puVar15 = *param_2;
              param_2 = param_2 + 1;
              puVar15 = puVar14;
            } while (puVar14 < puVar8 + iVar5);
          }
          puVar11 = puVar8 + (iVar3 - (int)puVar8) + 1;
          puVar8[iVar3 - (int)puVar8] = 0;
          iVar3 = (int)puVar11;
          if (0 < iVar6) {
            puVar10 = param_4;
            puVar8 = puVar11;
            puVar15 = puVar11;
            do {
              param_4 = (undefined8 *)((long)puVar10 + 1);
              puVar8 = puVar8 + 1;
              iVar3 = (int)puVar8;
              puVar14 = puVar15 + 1;
              *puVar15 = *(undefined1 *)puVar10;
              puVar10 = param_4;
              puVar15 = puVar14;
            } while (puVar14 < puVar11 + iVar6);
          }
          iVar5 = ((int)(puVar11 + (iVar3 - (int)puVar11)) - (int)&uStack_a8) + 1;
          puVar11[iVar3 - (int)puVar11] = (char)(iVar16 << 3);
          iVar3 = func_0x020a7f20();
          if (iVar3 == 0) {
            FUN_0208ada0(0x20c5578,0x20ba471);
            iVar3 = (int)param_8;
            uVar2 = 0;
            puVar10 = &uStack_68;
          }
          else {
            FUN_0208ada0(0x20c047b,0x20ba471);
            param_3 = (undefined8 *)0x0;
            param_4 = param_6;
            uVar2 = func_0x0209b824(&uStack_a8,iVar5);
            if (uVar2 != 0) {
              param_3 = (undefined8 *)(ulong)uVar2;
              FUN_0208ada0(0x20be1cf,0x20ba471);
              func_0x0209b670();
              func_0x0209b684();
            }
            iVar3 = (int)param_8;
            puVar10 = (undefined8 *)0x0;
          }
          if (iVar16 != 0) {
            cVar18 = '\x01';
            do {
              uStack_a8 = CONCAT71(uStack_a8._1_7_,cVar18);
              param_3 = puVar10;
              param_4 = param_6;
              uVar2 = func_0x0209b824(&uStack_a8,iVar5);
              iVar3 = (int)param_8;
              if (uVar2 != 0) {
                param_3 = (undefined8 *)(ulong)uVar2;
                FUN_0208ada0(0x20bc11d,0x20ba471);
                func_0x0209b670();
                func_0x0209b684();
                goto code_r0x0209f778;
              }
              cVar18 = cVar18 + '\x01';
              param_6 = param_6 + 2;
              uVar1 = (int)puVar17 - 0x10;
              puVar17 = (undefined8 *)(ulong)uVar1;
            } while (uVar1 != 0);
            uVar2 = 0;
          }
        }
      }
      else if (param_1 == 1) {
        uStack_78 = 0;
        uStack_70 = 0;
        uStack_88 = 0;
        uStack_80 = 0;
        uStack_98 = 0;
        uStack_90 = 0;
        uStack_a8 = 0;
        uStack_a0 = 0;
        uVar2 = 0x10200f;
        if (((param_7 & 0xf) == 0) && (0xffffffbf < uVar1 - 0x3e)) {
          if (iVar5 < 1) {
            puVar10 = &uStack_a8;
          }
          else {
            puVar9 = &uStack_a8;
            do {
              puVar10 = (undefined8 *)((long)puVar9 + 1);
              *(undefined1 *)puVar9 = *param_2;
              param_2 = param_2 + 1;
              puVar9 = puVar10;
            } while (puVar10 < (undefined8 *)((long)&uStack_a8 + (long)iVar5));
          }
          iVar5 = (int)puVar10 - (int)&uStack_a8;
          puVar8 = (undefined1 *)((long)&uStack_a8 + (long)iVar5 + 1);
          *(undefined1 *)((long)&uStack_a8 + (long)iVar5) = 0;
          iVar12 = (int)puVar8;
          if (0 < iVar6) {
            lVar13 = (long)&uStack_a8 + (long)iVar5 + 1;
            puVar11 = puVar8;
            do {
              lVar13 = lVar13 + 1;
              iVar12 = (int)lVar13;
              puVar15 = puVar11 + 1;
              *puVar11 = *(undefined1 *)param_4;
              param_4 = (undefined8 *)((long)param_4 + 1);
              puVar11 = puVar15;
            } while (puVar15 < puVar8 + iVar6);
          }
          puVar8[iVar12 - (int)puVar8] = (char)(iVar16 << 3);
          uVar2 = func_0x0209b928(&uStack_a8,
                                  ((int)(puVar8 + (iVar12 - (int)puVar8)) - (int)&uStack_a8) + 1);
          param_3 = param_6;
          param_4 = puVar17;
        }
      }
      else {
        uVar2 = 0x10200e;
      }
    }
  }
code_r0x0209f778:
  iVar6 = (int)puVar7;
  iVar5 = (int)param_4;
  if (lRam0000000002100880 == lStack_48) {
    return (ulong)uVar2;
  }
  auVar19 = FUN_02091090();
  lVar13 = auVar19._0_8_;
  if (((((lVar13 != 0) && (param_3 != (undefined8 *)0x0)) && (param_5 != 0)) &&
      ((param_7 != 0 && (auVar19._8_4_ != 0)))) && ((iVar5 != 0 && ((iVar6 != 0 && (iVar3 != 0))))))
  {
    if ((lVar13 != 0) &&
       ((((param_3 != (undefined8 *)0x0 && (param_5 != 0)) && (param_7 != 0)) &&
        (((auVar19._8_4_ != 0 && (iVar5 != 0)) && ((iVar6 != 0 && (iVar3 != 0)))))))) {
      uVar4 = func_0x020a91b4(0x18003000,lVar13,auVar19._8_8_ & 0xffffffff,param_3);
      return uVar4;
    }
    return 1;
  }
  return 0xffffffff;
}


