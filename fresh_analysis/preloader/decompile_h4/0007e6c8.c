// File offset entry: 0x7e6c8

ulong thunk_FUN_0207e3d0(undefined8 param_1,undefined8 param_2,ulong param_3)

{
  undefined8 uVar1;
  uint uVar2;
  uint uVar3;
  undefined8 uVar4;
  int iVar5;
  ulong uVar6;
  undefined8 uVar7;
  long lVar8;
  ulong uVar9;
  uint uVar10;
  ulong unaff_x19;
  ulong unaff_x20;
  ulong unaff_x21;
  ulong unaff_x22;
  undefined8 unaff_x23;
  long unaff_x24;
  ulong unaff_x25;
  undefined1 *unaff_x29;
  code *unaff_x30;
  undefined1 auVar11 [16];
  
  auVar11._8_8_ = param_2;
  auVar11._0_8_ = param_1;
  do {
    uVar9 = auVar11._0_8_;
    *(undefined1 **)((long)register0x00000008 + -0x50) = unaff_x29;
    *(code **)((long)register0x00000008 + -0x48) = unaff_x30;
    *(ulong *)((long)register0x00000008 + -0x40) = unaff_x25;
    *(long *)((long)register0x00000008 + -0x30) = unaff_x24;
    *(undefined8 *)((long)register0x00000008 + -0x28) = unaff_x23;
    *(ulong *)((long)register0x00000008 + -0x20) = unaff_x22;
    *(ulong *)((long)register0x00000008 + -0x18) = unaff_x21;
    *(ulong *)((long)register0x00000008 + -0x10) = unaff_x20;
    *(ulong *)((long)register0x00000008 + -8) = unaff_x19;
    unaff_x29 = (undefined1 *)((long)register0x00000008 + -0x50);
    unaff_x19 = 0x200002;
    *(undefined8 *)((long)register0x00000008 + -0x58) = *puRam0000000002103280;
    *(undefined8 *)((long)register0x00000008 + -0x68) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x60) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x78) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x70) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x88) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x80) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x98) = 0;
    *(undefined8 *)((long)register0x00000008 + -0x90) = 0;
    if (uVar9 == 0) {
code_r0x0207e58c:
      uVar9 = unaff_x20;
      if (lRam0000000002100880 == *(long *)((long)register0x00000008 + -0x58)) {
        return unaff_x19;
      }
    }
    else {
      unaff_x21 = param_3 & 0xffffffff;
      uVar10 = (uint)param_3;
      if (1 < uVar10) {
        unaff_x19 = 0x200001;
        goto code_r0x0207e58c;
      }
      unaff_x22 = auVar11._8_8_ & 0xffffffff;
      iVar5 = FUN_0207e344();
      unaff_x20 = uVar9;
      if (iVar5 == 1) {
        unaff_x19 = 0;
        goto code_r0x0207e58c;
      }
      if (iVar5 != -1) {
        uVar6 = func_0x02091524(uVar9);
        if (uVar6 < 0x40) {
          uVar7 = func_0x02091524(uVar9);
          func_0x020915ac((undefined1 *)((long)register0x00000008 + -0x98),uVar9,uVar7);
          lVar8 = func_0x02091524(uVar9);
          uVar7 = 0x20b313c;
          unaff_x23 = 0x20b313c;
          param_3 = 3;
          func_0x020915ac((undefined1 *)((long)register0x00000008 + lVar8 + -0x98),0x20b313c);
          *(undefined1 *)((long)register0x00000008 + -0x59) = 0;
          iVar5 = FUN_0207e344((undefined1 *)((long)register0x00000008 + -0x98));
          if (iVar5 == 0) {
            unaff_x19 = 0x200000;
          }
          else {
            lVar8 = func_0x02003f14();
            if (lVar8 == 0) {
              unaff_x19 = 0x200004;
            }
            else {
              unaff_x25 = func_0x02091524(uVar9);
              unaff_x23 = uVar7;
              unaff_x24 = lVar8;
              if (auVar11._8_4_ - 3U < (uint)unaff_x25) {
                unaff_x19 = 0x200003;
              }
              else {
                iVar5 = func_0x020915d8(lVar8,0x20b313c,2);
                unaff_x20 = uVar9 + (unaff_x25 & 0xffffffff);
                param_3 = 2;
                uVar1 = uVar7;
                uVar4 = 0x20cbdd2;
                if (uVar10 != 0) {
                  uVar1 = 0x20cbdd2;
                  uVar4 = uVar7;
                }
                if (iVar5 != 0) {
                  uVar1 = uVar4;
                }
                func_0x020915ac(unaff_x20,uVar1);
                unaff_x19 = 0;
                *(undefined1 *)(unaff_x20 + 2) = 0;
              }
            }
          }
        }
        else {
          param_3 = uVar9;
          func_0x0208ada0(0x20b8d43,0x20b0ae1,uVar9,0x40);
        }
        goto code_r0x0207e58c;
      }
      if (lRam0000000002100880 == *(long *)((long)register0x00000008 + -0x58)) {
        *(undefined8 *)((long)register0x00000008 + -0x30) =
             *(undefined8 *)((long)register0x00000008 + -0x50);
        *(undefined8 *)((long)register0x00000008 + -0x28) =
             *(undefined8 *)((long)register0x00000008 + -0x48);
        *(undefined8 *)((long)register0x00000008 + -0x20) =
             *(undefined8 *)((long)register0x00000008 + -0x20);
        *(undefined8 *)((long)register0x00000008 + -0x18) =
             *(undefined8 *)((long)register0x00000008 + -0x18);
        *(undefined8 *)((long)register0x00000008 + -0x10) =
             *(undefined8 *)((long)register0x00000008 + -0x10);
        *(undefined8 *)((long)register0x00000008 + -8) =
             *(undefined8 *)((long)register0x00000008 + -8);
        uVar2 = uRam000000000211bda8 >> 0x14;
        uVar3 = uRam000000000211bda8 >> 0xc;
        uVar10 = uRam000000000211bda8 & 0xfff;
        func_0x0208ada0(0x20ce5a8,uRam000000000211bda8);
        func_0x0208ada0(0x20c714a,uVar2 & 0x7ff);
        func_0x0208ada0(0x20cd30d,uVar3 & 0xff);
        func_0x0208ada0(0x20c5ec9,uVar10);
        return (ulong)uRam000000000211bda8;
      }
    }
    unaff_x30 = thunk_FUN_0207e3d0;
    auVar11 = func_0x02091090();
    register0x00000008 = (BADSPACEBASE *)((long)register0x00000008 + -0xa0);
    unaff_x20 = uVar9;
  } while( true );
}


