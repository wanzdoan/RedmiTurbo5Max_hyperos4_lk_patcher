// File offset entry: 0x6a1d0

void FUN_0206a0d0(undefined8 param_1,undefined8 param_2,ulong param_3,ulong param_4)

{
  undefined1 auVar1 [16];
  undefined1 auVar2 [16];
  undefined1 *puVar3;
  ulong uVar4;
  undefined8 unaff_x19;
  ulong unaff_x20;
  ulong unaff_x21;
  undefined8 unaff_x22;
  undefined8 unaff_x23;
  undefined8 unaff_x28;
  undefined1 *unaff_x29;
  code *unaff_x30;
  undefined1 auVar5 [16];
  
  auVar2._8_8_ = unaff_x22;
  auVar2._0_8_ = unaff_x23;
  auVar5._8_8_ = param_2;
  auVar5._0_8_ = param_1;
  while( true ) {
    auVar1 = auVar5;
    uVar4 = auVar1._8_8_;
    puVar3 = (undefined1 *)((long)register0x00000008 + -0x40);
    *(undefined1 **)((long)register0x00000008 + -0x40) = unaff_x29;
    *(code **)((long)register0x00000008 + -0x38) = unaff_x30;
    *(undefined8 *)((long)register0x00000008 + -0x30) = unaff_x28;
    *(undefined1 (*) [16])((long)register0x00000008 + -0x28) = auVar2;
    *(ulong *)((long)register0x00000008 + -0x18) = unaff_x21;
    *(ulong *)((long)register0x00000008 + -0x10) = unaff_x20;
    *(undefined8 *)((long)register0x00000008 + -8) = unaff_x19;
    unaff_x21 = param_3 & 0xffffffff;
    unaff_x19 = 1;
    unaff_x20 = param_4 & 0xffffffff;
    *(undefined8 *)((long)register0x00000008 + -0x48) = *puRam0000000002103280;
    FUN_02091664((undefined1 *)((long)register0x00000008 + -0x468),0,0x420);
    param_4 = unaff_x21;
    func_0x02068634((undefined1 *)((long)register0x00000008 + -0x468),auVar1._0_8_,uVar4,unaff_x21,
                    unaff_x20,1);
    if (lRam0000000002100880 == *(long *)((long)register0x00000008 + -0x48)) break;
    unaff_x30 = FUN_0206a0d0;
    auVar5 = FUN_02091090();
    register0x00000008 = (BADSPACEBASE *)((long)register0x00000008 + -0x470);
    param_3 = uVar4;
    unaff_x29 = puVar3;
    auVar2 = auVar1;
  }
  return;
}


