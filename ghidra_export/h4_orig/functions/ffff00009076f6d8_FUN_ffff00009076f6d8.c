// ffff00009076f6d8  size=816  FUN_ffff00009076f6d8

int FUN_ffff00009076f6d8(ushort *param_1,undefined8 *param_2,int param_3,uint param_4)

{
  ushort *puVar1;
  undefined8 *puVar2;
  undefined4 *puVar3;
  undefined8 uVar4;
  byte bVar5;
  ushort uVar6;
  ushort *puVar7;
  ulong uVar8;
  long lVar9;
  undefined8 *puVar10;
  undefined8 *puVar11;
  ulong uVar12;
  undefined8 *puVar13;
  int iVar14;
  undefined8 *puVar15;
  ulong uVar16;
  ushort *puVar17;
  undefined8 *puVar18;
  ushort *puVar19;
  ushort *puVar20;
  undefined8 *puVar21;
  undefined8 *puVar22;
  long lVar23;
  
  iVar14 = -1;
  if ((param_1 != (ushort *)0x0) && (-1 < (int)param_4)) {
    if (param_4 == 0) {
      if (param_3 == 1) {
        iVar14 = -(uint)((byte)*param_1 != 0);
      }
    }
    else if (param_3 != 0) {
      puVar1 = (ushort *)((long)param_1 + (long)param_3);
      puVar2 = (undefined8 *)((long)param_2 + (ulong)param_4);
      puVar17 = (ushort *)((long)puVar1 - 0xf);
      puVar18 = (undefined8 *)((long)puVar2 + -7);
      puVar7 = param_1;
      puVar15 = param_2;
      do {
        while( true ) {
          puVar19 = (ushort *)((long)puVar7 + 1);
          uVar12 = (ulong)(byte)*puVar7;
          bVar5 = (byte)*puVar7 >> 4;
          uVar8 = (ulong)bVar5;
          if (bVar5 == 0xf) break;
          if ((puVar1 + -8 <= puVar19) || (puVar2 + -4 < puVar15)) goto LAB_ffff00009076f7e0;
          uVar4 = *(undefined8 *)((long)puVar7 + 9);
          puVar7 = (ushort *)((long)puVar19 + uVar8);
          puVar13 = (undefined8 *)((long)puVar15 + uVar8);
          *puVar15 = *(undefined8 *)puVar19;
          puVar15[1] = uVar4;
          uVar16 = uVar12 & 0xf;
          puVar19 = puVar7 + 1;
          uVar6 = *puVar7;
          uVar8 = (ulong)uVar6;
          puVar10 = (undefined8 *)((long)puVar13 - uVar8);
          if ((((int)uVar16 == 0xf) || (uVar6 < 8)) || (puVar10 < param_2)) {
            if (uVar16 == 0xf) goto LAB_ffff00009076f818;
LAB_ffff00009076f8b4:
            uVar12 = uVar12 & 0xf;
            goto LAB_ffff00009076f8b8;
          }
          puVar15 = (undefined8 *)((long)puVar13 + uVar16 + 4);
          *puVar13 = *puVar10;
          puVar13[1] = puVar10[1];
          *(undefined2 *)(puVar13 + 2) = *(undefined2 *)(puVar10 + 2);
          puVar7 = puVar19;
        }
        if (puVar17 <= puVar19) goto LAB_ffff00009076f9cc;
        lVar9 = 0;
        lVar23 = 0;
        if (puVar19 <= puVar17) {
          lVar23 = (long)puVar17 - (long)puVar19;
        }
        puVar7 = puVar7 + 1;
        puVar20 = puVar19;
        do {
          puVar19 = (ushort *)((long)puVar20 + 1);
          if (lVar23 == 0) goto LAB_ffff00009076f9cc;
          uVar6 = *puVar20;
          lVar23 = lVar23 + -1;
          puVar7 = (ushort *)((long)puVar7 + 1);
          lVar9 = lVar9 + (ulong)(byte)uVar6;
          puVar20 = puVar19;
        } while ((ulong)(byte)uVar6 == 0xff);
        if (((lVar9 == -1) || (uVar8 = lVar9 + 0xf, CARRY8((ulong)puVar15,uVar8))) ||
           ((ulong)-(long)puVar7 < uVar8)) goto LAB_ffff00009076f9cc;
LAB_ffff00009076f7e0:
        puVar13 = (undefined8 *)((long)puVar15 + uVar8);
        puVar7 = (ushort *)((long)puVar19 + uVar8);
        if ((undefined8 *)((long)puVar2 + -0xc) < puVar13 || puVar1 + -4 < puVar7) {
          if ((puVar7 == puVar1) && (puVar13 <= puVar2)) {
            FUN_ffff00009077ec38(puVar15);
            return (int)puVar13 - (int)param_2;
          }
LAB_ffff00009076f9cc:
          return ~(uint)puVar19 + (int)param_1;
        }
        do {
          puVar10 = puVar15 + 1;
          *puVar15 = *(undefined8 *)puVar19;
          puVar19 = puVar19 + 4;
          puVar15 = puVar10;
        } while (puVar10 < puVar13);
        puVar19 = puVar7 + 1;
        uVar8 = (ulong)*puVar7;
        puVar10 = (undefined8 *)((long)puVar13 - uVar8);
        if ((uVar12 & 0xf) != 0xf) goto LAB_ffff00009076f8b4;
LAB_ffff00009076f818:
        lVar9 = 0;
        lVar23 = 0;
        puVar7 = puVar19;
        if (puVar19 <= puVar1 + -2) {
          lVar23 = (long)(puVar1 + -2) - (long)puVar19;
        }
        do {
          puVar19 = (ushort *)((long)puVar7 + 1);
          if (lVar23 == 0) goto LAB_ffff00009076f9cc;
          uVar6 = *puVar7;
          lVar9 = lVar9 + (ulong)(byte)uVar6;
          lVar23 = lVar23 + -1;
          puVar7 = puVar19;
        } while ((ulong)(byte)uVar6 == 0xff);
        uVar12 = lVar9 + 0xf;
        if ((lVar9 == -1) || (CARRY8((ulong)puVar13,uVar12))) goto LAB_ffff00009076f9cc;
LAB_ffff00009076f8b8:
        if (puVar10 < param_2) goto LAB_ffff00009076f9cc;
        if (uVar8 < 8) {
          *(undefined4 *)puVar13 = 0;
          *(undefined1 *)puVar13 = *(undefined1 *)puVar10;
          *(undefined1 *)((long)puVar13 + 1) = *(undefined1 *)((long)puVar10 + 1);
          *(undefined1 *)((long)puVar13 + 2) = *(undefined1 *)((long)puVar10 + 2);
          *(undefined1 *)((long)puVar13 + 3) = *(undefined1 *)((long)puVar10 + 3);
          puVar3 = (undefined4 *)
                   ((long)puVar10 + (ulong)*(uint *)((long)&DAT_ffff000090803fb0 + uVar8 * 4));
          *(undefined4 *)((long)puVar13 + 4) = *puVar3;
          puVar11 = (undefined8 *)((long)puVar3 - (long)*(int *)(&DAT_ffff000090803f30 + uVar8 * 4))
          ;
        }
        else {
          puVar11 = puVar10 + 1;
          *puVar13 = *puVar10;
        }
        puVar10 = puVar13 + 1;
        puVar15 = (undefined8 *)((long)puVar13 + uVar12 + 4);
        puVar7 = puVar19;
        if ((undefined8 *)((long)puVar2 + -0xc) < puVar15) {
          if ((undefined8 *)((long)puVar2 + -5) < puVar15) goto LAB_ffff00009076f9cc;
          puVar13 = puVar10;
          puVar22 = puVar11;
          if (puVar10 < puVar18) {
            do {
              puVar21 = puVar13 + 1;
              *puVar13 = *puVar22;
              puVar13 = puVar21;
              puVar22 = puVar22 + 1;
            } while (puVar21 < puVar18);
            puVar11 = (undefined8 *)((long)puVar11 + ((long)puVar18 - (long)puVar10));
            puVar10 = puVar18;
          }
          for (; puVar10 < puVar15; puVar10 = (undefined8 *)((long)puVar10 + 1)) {
            *(undefined1 *)puVar10 = *(undefined1 *)puVar11;
            puVar11 = (undefined8 *)((long)puVar11 + 1);
          }
        }
        else {
          *puVar10 = *puVar11;
          if (0x10 < uVar12 + 4) {
            puVar13 = puVar13 + 2;
            do {
              puVar11 = puVar11 + 1;
              puVar10 = puVar13 + 1;
              *puVar13 = *puVar11;
              puVar13 = puVar10;
            } while (puVar10 < puVar15);
          }
        }
      } while( true );
    }
  }
  return iVar14;
}


