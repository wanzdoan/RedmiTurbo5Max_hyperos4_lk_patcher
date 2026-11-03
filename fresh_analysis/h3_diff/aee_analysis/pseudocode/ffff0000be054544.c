
void FUN_ffff0000be054544(uint *param_1,uint *param_2,ulong *param_3)

{
  ulong *puVar1;
  ulong *puVar2;
  ulong *puVar3;
  uint uVar4;
  uint uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong *puVar14;
  ulong uVar15;
  ulong uVar16;
  ulong uVar17;
  ulong uVar18;
  ulong uVar19;
  ulong uVar20;
  ulong uVar21;
  
  if (((param_1 != (uint *)0x0) && (param_2 != (uint *)0x0)) && (param_3 != (ulong *)0x0)) {
    puVar14 = param_3 + 6;
    uVar4 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
    uVar5 = (param_1[1] & 0xff00ff00) >> 8 | (param_1[1] & 0xff00ff) << 8;
    uVar7 = (ulong)(uVar4 >> 0x10 | uVar4 << 0x10) ^ *param_3;
    uVar15 = (ulong)(uVar5 >> 0x10 | uVar5 << 0x10) ^ param_3[1];
    uVar4 = (param_1[2] & 0xff00ff00) >> 8 | (param_1[2] & 0xff00ff) << 8;
    uVar5 = (param_1[3] & 0xff00ff00) >> 8 | (param_1[3] & 0xff00ff) << 8;
    uVar17 = (ulong)(uVar4 >> 0x10 | uVar4 << 0x10) ^ param_3[2];
    uVar12 = (ulong)(((int)param_3[0x3c] >> 1) - 1);
    uVar6 = (ulong)(uVar5 >> 0x10 | uVar5 << 0x10) ^ param_3[3];
    uVar13 = uVar12;
    while( true ) {
      uVar11 = *(ulong *)(&LAB_ffff0000be0cd928 + (uVar15 >> 0x10 & 0xff) * 8) ^
               *(ulong *)(&DAT_ffff0000be0ce128 + (uVar17 >> 8 & 0xff) * 8) ^
               *(ulong *)(&DAT_ffff0000be0cd128 + (uVar7 >> 0x15 & 0x7fffffffff8)) ^
               *(ulong *)(&DAT_ffff0000be0ce928 + (uVar6 & 0xff) * 8) ^ puVar14[-2];
      uVar9 = *(ulong *)(&LAB_ffff0000be0cd928 + (uVar17 >> 0x10 & 0xff) * 8) ^
              *(ulong *)(&DAT_ffff0000be0ce128 + (uVar6 >> 8 & 0xff) * 8) ^
              *(ulong *)(&DAT_ffff0000be0cd128 + (uVar15 >> 0x15 & 0x7fffffffff8)) ^
              *(ulong *)(&DAT_ffff0000be0ce928 + (uVar7 & 0xff) * 8) ^ puVar14[-1];
      uVar21 = uVar9 >> 0x10 & 0xff;
      uVar16 = *(ulong *)(&LAB_ffff0000be0cd928 + (uVar6 >> 0x10 & 0xff) * 8) ^
               *(ulong *)(&DAT_ffff0000be0ce128 + (uVar7 >> 8 & 0xff) * 8) ^
               *(ulong *)(&DAT_ffff0000be0cd128 + (uVar17 >> 0x15 & 0x7fffffffff8)) ^
               *(ulong *)(&DAT_ffff0000be0ce928 + (uVar15 & 0xff) * 8) ^ *puVar14;
      uVar20 = uVar16 >> 8 & 0xff;
      uVar8 = *(ulong *)(&LAB_ffff0000be0cd928 + (uVar7 >> 0x10 & 0xff) * 8) ^
              *(ulong *)(&DAT_ffff0000be0ce128 + (uVar15 >> 8 & 0xff) * 8) ^
              *(ulong *)(&DAT_ffff0000be0cd128 + (uVar6 >> 0x15 & 0x7fffffffff8)) ^
              *(ulong *)(&DAT_ffff0000be0ce928 + (uVar17 & 0xff) * 8) ^ puVar14[1];
      uVar19 = uVar16 >> 0x10 & 0xff;
      uVar15 = uVar8 >> 8 & 0xff;
      uVar10 = uVar8 >> 0x10 & 0xff;
      uVar17 = uVar11 >> 8 & 0xff;
      uVar6 = uVar11 >> 0x10 & 0xff;
      uVar18 = uVar9 >> 8 & 0xff;
      if ((int)uVar13 == 0) break;
      puVar2 = puVar14 + 3;
      uVar7 = *(ulong *)(&LAB_ffff0000be0cd928 + uVar21 * 8) ^
              *(ulong *)(&DAT_ffff0000be0ce128 + uVar20 * 8) ^
              *(ulong *)(&DAT_ffff0000be0cd128 + (uVar11 >> 0x15 & 0x7fffffffff8)) ^
              *(ulong *)(&DAT_ffff0000be0ce928 + (uVar8 & 0xff) * 8) ^ puVar14[2];
      uVar13 = (ulong)((int)uVar13 - 1);
      puVar1 = puVar14 + 4;
      puVar3 = puVar14 + 5;
      puVar14 = puVar14 + 8;
      uVar15 = *(ulong *)(&LAB_ffff0000be0cd928 + uVar19 * 8) ^
               *(ulong *)(&DAT_ffff0000be0ce128 + uVar15 * 8) ^
               *(ulong *)(&DAT_ffff0000be0cd128 + (uVar9 >> 0x15 & 0x7fffffffff8)) ^
               *(ulong *)(&DAT_ffff0000be0ce928 + (uVar11 & 0xff) * 8) ^ *puVar2;
      uVar17 = *(ulong *)(&LAB_ffff0000be0cd928 + uVar10 * 8) ^
               *(ulong *)(&DAT_ffff0000be0ce128 + uVar17 * 8) ^
               *(ulong *)(&DAT_ffff0000be0cd128 + (uVar16 >> 0x15 & 0x7fffffffff8)) ^
               *(ulong *)(&DAT_ffff0000be0ce928 + (uVar9 & 0xff) * 8) ^ *puVar1;
      uVar6 = *(ulong *)(&LAB_ffff0000be0cd928 + uVar6 * 8) ^
              *(ulong *)(&DAT_ffff0000be0ce128 + uVar18 * 8) ^
              *(ulong *)(&DAT_ffff0000be0cd128 + (uVar8 >> 0x15 & 0x7fffffffff8)) ^
              *(ulong *)(&DAT_ffff0000be0ce928 + (uVar16 & 0xff) * 8) ^ *puVar3;
    }
    uVar4 = ((uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + uVar21 * 8) & 0xff0000 |
             (uint)(byte)(&DAT_ffff0000be0cc8db)[(uVar11 >> 0x18) * 8] << 0x18 |
             (uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + uVar20 * 8) & 0xff00 |
            (uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + (uVar8 & 0xff) * 8) & 0xff) ^
            (uint)param_3[uVar12 * 8 + 8];
    uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
    *param_2 = uVar4 >> 0x10 | uVar4 << 0x10;
    uVar4 = ((uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + uVar19 * 8) & 0xff0000 |
             (uint)(byte)(&DAT_ffff0000be0cc8db)[(uVar9 >> 0x18) * 8] << 0x18 |
             (uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + uVar15 * 8) & 0xff00 |
            (uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + (uVar11 & 0xff) * 8) & 0xff) ^
            (uint)param_3[uVar12 * 8 + 9];
    uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
    param_2[1] = uVar4 >> 0x10 | uVar4 << 0x10;
    uVar4 = ((uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + uVar10 * 8) & 0xff0000 |
             (uint)(byte)(&DAT_ffff0000be0cc8db)[(uVar16 >> 0x18) * 8] << 0x18 |
             (uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + uVar17 * 8) & 0xff00 |
            (uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + (uVar9 & 0xff) * 8) & 0xff) ^
            (uint)param_3[uVar12 * 8 + 10];
    uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
    param_2[2] = uVar4 >> 0x10 | uVar4 << 0x10;
    uVar4 = ((uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + uVar6 * 8) & 0xff0000 |
             (uint)(byte)(&DAT_ffff0000be0cc8db)[(uVar8 >> 0x18) * 8] << 0x18 |
             (uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + uVar18 * 8) & 0xff00 |
            (uint)*(undefined8 *)(&DAT_ffff0000be0cc8d8 + (uVar16 & 0xff) * 8) & 0xff) ^
            (uint)param_3[uVar12 * 8 + 0xb];
    uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
    param_2[3] = uVar4 >> 0x10 | uVar4 << 0x10;
  }
  return;
}


