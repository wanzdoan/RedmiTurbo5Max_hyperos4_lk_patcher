// ffff00009077cc1c  size=416  FUN_ffff00009077cc1c

undefined4 FUN_ffff00009077cc1c(undefined8 param_1,undefined8 param_2,uint param_3,uint param_4)

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
  
  lVar2 = FUN_ffff00009077bf18();
  if (lVar2 == 0) {
    lVar3 = 0;
    uVar12 = 0xfffffffe;
  }
  else {
    lVar3 = FUN_ffff00009077d514(0xd8);
    if (lVar3 == 0) {
      uVar12 = 0xfffffffb;
    }
    else {
      lVar7 = *(long *)(lVar2 + 0x58);
      if ((lVar7 == 0) || (puVar10 = *(ulong **)(lVar2 + 0x60), puVar10 == (ulong *)0x0)) {
        if ((!CARRY4(param_4,param_3)) && (param_4 + param_3 <= *(uint *)(lVar2 + 0x38))) {
          uVar4 = 0;
          puVar5 = (undefined8 *)0x0;
LAB_ffff00009077cd18:
          FUN_ffff00009077c230
                    (lVar3,param_2,*(undefined8 *)(lVar2 + 0x28),(ulong)param_4,uVar4,puVar5,0);
          uVar1 = *(undefined1 *)(lVar2 + 0x68);
          *(uint *)(lVar3 + 0xd0) = param_3;
          *(undefined1 *)(lVar3 + 0x68) = uVar1;
          *(code **)(lVar3 + 0x70) = FUN_ffff00009077cdbc;
          *(code **)(lVar3 + 0x78) = FUN_ffff00009077cdd8;
          *(code **)(lVar3 + 0x80) = FUN_ffff00009077cdf0;
          *(code **)(lVar3 + 0x88) = FUN_ffff00009077ce0c;
          *(code **)(lVar3 + 0x90) = FUN_ffff00009077ce24;
          *(code **)(lVar3 + 0xa0) = FUN_ffff00009077ce40;
          *(long *)(lVar3 + 0xa8) = lVar2;
          FUN_ffff00009077c9f0(lVar3);
          return 0;
        }
      }
      else {
        uVar11 = (ulong)param_3 << (*(ulong *)(lVar2 + 0x30) & 0x3f);
        uVar6 = (ulong)param_4 << (*(ulong *)(lVar2 + 0x30) & 0x3f);
        puVar9 = puVar10 + lVar7 * 4 + -4;
        do {
          if (*puVar10 <= uVar11 && uVar6 + uVar11 <= puVar10[1] + *puVar10)
          goto LAB_ffff00009077ccec;
          puVar10 = puVar10 + 4;
          lVar7 = lVar7 + -1;
        } while (lVar7 != 0);
        puVar10 = puVar9;
        if (puVar9 != (ulong *)0x0) {
LAB_ffff00009077ccec:
          uVar8 = puVar10[3];
          if (((uVar11 | uVar6) & (-1L << (uVar8 & 0x3f) ^ 0xffffffffffffffffU)) == 0) {
            uVar11 = puVar10[2];
            uVar4 = 1;
            puVar5 = (undefined8 *)(lVar3 + 0xb0);
            *puVar5 = 0;
            *(ulong *)(lVar3 + 0xb8) = uVar6;
            *(ulong *)(lVar3 + 0xc0) = uVar11;
            *(ulong *)(lVar3 + 200) = uVar8;
            goto LAB_ffff00009077cd18;
          }
        }
      }
      uVar12 = 0xfffffff8;
    }
    FUN_ffff00009077bfac(lVar2);
  }
  thunk_FUN_ffff0000907c314c(lVar3);
  return uVar12;
}


