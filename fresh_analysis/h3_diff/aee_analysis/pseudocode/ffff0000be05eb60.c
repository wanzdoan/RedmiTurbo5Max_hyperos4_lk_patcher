
undefined4 FUN_ffff0000be05eb60(undefined8 param_1,undefined8 param_2,uint param_3,uint param_4)

{
  undefined1 uVar1;
  long lVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 *puVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  ulong *puVar9;
  ulong *puVar10;
  ulong uVar11;
  undefined4 uVar12;
  
  lVar2 = FUN_ffff0000be05de5c();
  if (lVar2 == 0) {
    lVar3 = 0;
    uVar12 = 0xfffffffe;
  }
  else {
    lVar3 = FUN_ffff0000be05f1bc(0xd8);
    if (lVar3 == 0) {
      uVar12 = 0xfffffffb;
    }
    else {
      lVar7 = *(long *)(lVar2 + 0x58);
      if ((lVar7 == 0) || (puVar10 = *(ulong **)(lVar2 + 0x60), puVar10 == (ulong *)0x0)) {
        if ((!CARRY4(param_4,param_3)) && (param_4 + param_3 <= *(uint *)(lVar2 + 0x38))) {
          uVar4 = 0;
          puVar5 = (undefined8 *)0x0;
LAB_ffff0000be05ec5c:
          FUN_ffff0000be05e174
                    (lVar3,param_2,*(undefined8 *)(lVar2 + 0x28),(ulong)param_4,uVar4,puVar5,0);
          uVar1 = *(undefined1 *)(lVar2 + 0x68);
          *(uint *)(lVar3 + 0xd0) = param_3;
          *(undefined1 *)(lVar3 + 0x68) = uVar1;
          *(undefined1 **)(lVar3 + 0x70) = &LAB_ffff0000be05ed00;
          *(undefined1 **)(lVar3 + 0x78) = &LAB_ffff0000be05ed1c;
          *(undefined1 **)(lVar3 + 0x80) = &LAB_ffff0000be05ed34;
          *(undefined1 **)(lVar3 + 0x88) = &LAB_ffff0000be05ed50;
          *(undefined1 **)(lVar3 + 0x90) = &LAB_ffff0000be05ed68;
          *(code **)(lVar3 + 0xa0) = FUN_ffff0000be05ed84;
          *(long *)(lVar3 + 0xa8) = lVar2;
          FUN_ffff0000be05e934(lVar3);
          return 0;
        }
      }
      else {
        uVar11 = (ulong)param_3 << (*(ulong *)(lVar2 + 0x30) & 0x3f);
        uVar6 = (ulong)param_4 << (*(ulong *)(lVar2 + 0x30) & 0x3f);
        puVar9 = puVar10 + lVar7 * 4 + -4;
        do {
          if (*puVar10 <= uVar11 && uVar6 + uVar11 <= puVar10[1] + *puVar10)
          goto LAB_ffff0000be05ec30;
          puVar10 = puVar10 + 4;
          lVar7 = lVar7 + -1;
        } while (lVar7 != 0);
        puVar10 = puVar9;
        if (puVar9 != (ulong *)0x0) {
LAB_ffff0000be05ec30:
          uVar8 = puVar10[3];
          if (((uVar11 | uVar6) & (-1L << (uVar8 & 0x3f) ^ 0xffffffffffffffffU)) == 0) {
            uVar11 = puVar10[2];
            uVar4 = 1;
            puVar5 = (undefined8 *)(lVar3 + 0xb0);
            *puVar5 = 0;
            *(ulong *)(lVar3 + 0xb8) = uVar6;
            *(ulong *)(lVar3 + 0xc0) = uVar11;
            *(ulong *)(lVar3 + 200) = uVar8;
            goto LAB_ffff0000be05ec5c;
          }
        }
      }
      uVar12 = 0xfffffff8;
    }
    FUN_ffff0000be05def0(lVar2);
  }
  thunk_FUN_ffff0000be09937c(lVar3);
  return uVar12;
}


