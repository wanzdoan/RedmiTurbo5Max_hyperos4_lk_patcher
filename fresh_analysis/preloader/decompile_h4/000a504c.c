// File offset entry: 0xa504c

int FUN_020a4f4c(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  long lVar2;
  bool bVar3;
  int iVar4;
  int iVar5;
  ulong uVar6;
  undefined8 uVar7;
  undefined8 *puVar8;
  ulong uVar9;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  undefined8 uStack_70;
  long lStack_68;
  
  lStack_68 = *plRam0000000002103280;
  uVar6 = func_0x0208cbd0();
  uVar6 = uVar6 & 0xffffffff;
  uStack_78 = 0;
  uStack_70 = 0;
  uStack_88 = 0;
  uStack_80 = 0;
  uStack_98 = 0;
  uStack_90 = 0;
  iRam0000000002122cc8 = iRam0000000002122cc8 + 1;
  uStack_a8 = 0;
  uStack_a0 = 0;
  uStack_b8 = 0;
  uStack_b0 = 0;
  uStack_c8 = 0;
  uStack_c0 = 0;
  func_0x02091664(0x2123170,0,0x8c);
  func_0x02091664(lRam00000000021037f0,0,0x8c);
  lVar1 = lRam00000000021037f8;
  func_0x02091664(lRam00000000021037f8,0,0x464);
  *(undefined4 *)(lVar1 + 0x200) = 0x200;
  *(undefined4 *)(lVar1 + 0x404) = 0x200;
  *(undefined4 *)(lVar1 + 0x448) = 0x30;
  *(undefined4 *)(lVar1 + 0x450) = 4;
  *(undefined4 *)(lVar1 + 0x40c) = 4;
  uVar9 = uRam0000000002103800;
  func_0x02091664(uRam0000000002103800,0,0x154);
  *(undefined4 *)(uVar9 + 0x38) = 0x30;
  *(undefined4 *)(uVar9 + 0x6c) = 0x30;
  *(undefined4 *)(uVar9 + 0x74) = 4;
  *(undefined4 *)(uVar9 + 0x7c) = 4;
  *(undefined4 *)(uVar9 + 4) = 4;
  uVar7 = func_0x0208cbb0();
  func_0x02091664(uVar7,0,uVar6);
  func_0x020a7c90(param_1,param_2);
  lRam0000000002122cd0 = func_0x0209aca8(0x200);
  if (lRam0000000002122cd0 == 0) {
    iVar4 = 0x100006;
  }
  else {
    uVar6 = 0;
    bVar3 = false;
    while (uVar9 = uVar6, !bVar3) {
      iVar4 = func_0x0208cbfc(param_1,uVar7,uVar6,0x800);
      if (iVar4 != 0) goto LAB_020a5320;
      iVar4 = func_0x02066430(uVar7,0x800,0x2123170);
      if (iVar4 != 0) goto LAB_020a52f4;
      bVar3 = false;
      if (iRam0000000002123170 == 1) {
        iVar4 = FUN_020a4ee4(param_2);
        bVar3 = iVar4 == 0;
        uVar6 = uVar6 + uRam0000000002123174 + (ulong)uRam0000000002123194;
      }
    }
    func_0x020915ac(lRam0000000002122cd0,uVar7,0x200);
    iVar4 = FUN_020a4c6c(uVar7,uRam0000000002123174,0,&uStack_88,0x20);
    if ((iVar4 == 0) &&
       (iVar4 = FUN_020a4c6c(uVar7,uRam0000000002123174,0,&uStack_c8,0x30), iVar4 == 0)) {
      iVar4 = func_0x0208cbfc(param_1,uVar7,uVar6,0x800);
      if (iVar4 != 0) goto LAB_020a5320;
      iVar4 = func_0x02066430(uVar7,0x800,lRam00000000021037f0);
      lVar1 = lRam00000000021037f0;
      if ((((iVar4 == 0) && (iVar4 = FUN_020a4b58(lRam00000000021037f0), iVar4 == 0)) &&
          (iVar4 = func_0x0208cbfc(param_1,uVar7,uVar6,*(int *)(lVar1 + 0x24) + *(int *)(lVar1 + 4))
          , lVar2 = lRam00000000021037f8, iVar4 == 0)) &&
         (iVar4 = func_0x020a5554(uVar7,*(int *)(lVar1 + 0x24) + *(int *)(lVar1 + 4),
                                  lRam00000000021037f8), iVar4 == 0)) {
        iVar4 = *(int *)(lVar2 + 0x44c);
        FUN_0208c834(param_1);
        iVar5 = func_0x0208cb38();
        if (iVar5 == iVar4) {
          uVar6 = uVar6 + *(uint *)(lVar1 + 4) + (ulong)*(uint *)(lVar1 + 0x24);
          iVar4 = func_0x0208cbfc(param_1,uVar7,uVar6,0x800);
          uVar9 = uVar6;
          if (iVar4 != 0) goto LAB_020a5320;
          iVar4 = func_0x02066430(uVar7,0x800,lRam00000000021037f0);
          lVar1 = lRam00000000021037f0;
          if (((iVar4 == 0) && (iVar4 = FUN_020a4be0(lRam00000000021037f0), iVar4 == 0)) &&
             ((iVar4 = func_0x0208cbfc(param_1,uVar7,uVar6,
                                       *(int *)(lVar1 + 0x24) + *(int *)(lVar1 + 4)),
              uVar9 = uRam0000000002103800, iVar4 == 0 &&
              (iVar4 = func_0x020a6408(uVar7,*(int *)(lVar1 + 0x24) + *(int *)(lVar1 + 4),
                                       lRam00000000021037f8,uRam0000000002103800), uVar6 = uVar9,
              iVar4 == 0)))) {
            if (uRam000000000212315c == 0) goto LAB_020a536c;
            if (uRam000000000212315c < 3) {
              puVar8 = &uStack_c8;
              uVar7 = 0x30;
              goto LAB_020a5378;
            }
            iVar4 = 0x10101b;
          }
        }
        else {
          iVar4 = 0x101000;
        }
      }
    }
  }
LAB_020a52f4:
  do {
    func_0x0208ada0(0x20cdbba,0x20c9773,param_1,param_2);
    func_0x0208ada0(0x20d01cb,iVar4);
    uVar9 = uVar6;
LAB_020a5320:
    while( true ) {
      if (*plRam0000000002103280 == lStack_68) {
        return iVar4;
      }
      func_0x02091090();
LAB_020a536c:
      puVar8 = &uStack_88;
      uVar7 = 0x20;
LAB_020a5378:
      iVar5 = func_0x020a0e58(uVar9 + 0x3c,puVar8,uVar7);
      iVar4 = 0x101003;
      uVar6 = uVar9;
      if (iVar5 != 0) break;
      func_0x0208ada0(0x20cdbba,0x20c9773,param_1,param_2);
      func_0x0208ada0(0x20cc927);
      iVar4 = 0;
    }
  } while( true );
}


