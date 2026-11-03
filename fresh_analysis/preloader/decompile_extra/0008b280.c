// File offset entry: 0x8b280

/* WARNING: Removing unreachable block (ram,0x02068200) */

void FUN_0208b180(ulong param_1,ulong param_2)

{
  long lVar1;
  undefined1 uVar2;
  long *plVar3;
  undefined1 *puVar4;
  int iVar5;
  int iVar6;
  undefined8 extraout_x1;
  undefined8 extraout_x1_00;
  uint uVar7;
  undefined8 in_x6;
  undefined8 in_x7;
  int extraout_w8;
  int extraout_w8_00;
  ulong uVar8;
  long extraout_x9;
  undefined1 extraout_w10;
  undefined8 unaff_x29;
  undefined8 unaff_x30;
  undefined1 auVar9 [16];
  
  if (0xf < param_1 || 1 < param_2) {
    lVar1 = 0;
    if (param_2 != 0) {
      lVar1 = param_2 - 1;
    }
    FUN_0208b180(param_1 >> 4,lVar1);
    param_1 = param_1 & 0xf;
  }
  iVar5 = 0x30;
  if (9 < param_1) {
    iVar5 = 0x37;
  }
  iVar5 = iVar5 + (int)param_1;
  uVar2 = (undefined1)iVar5;
  if (iRam000000000211d9f0 == 0) {
    if ((iRam00000000020eccd0 != 0) && (iVar6 = FUN_0200c72c(), iVar6 == 0)) {
      FUN_020924c8(iVar5);
    }
  }
  else if (puRam000000000211da00 < puRam000000000211da08) {
    puVar4 = puRam000000000211da00 + 1;
    *puRam000000000211da00 = uVar2;
    puRam000000000211da00 = puVar4;
  }
  else {
    iRam000000000211da10 = iRam000000000211da10 + 1;
  }
  FUN_0200c140(iVar5);
  plVar3 = plRam00000000021036f0;
  if (iRam00000000020ecccc != 0) {
    if (cRam000000000211be35 == '\x01') {
      if ((((iRam000000000011df0c == 0x5678ef90) && ((bRam000000000211be68 & 1) == 0)) &&
          (iRam00000000020eccc8 == 1)) &&
         (uVar8 = (ulong)uRam000000000011df6c, uRam000000000011df6c < 0x17000)) {
        uRam000000000011df6c = uRam000000000011df6c + 1;
        *(undefined1 *)(*plRam00000000021036f0 + uVar8) = uVar2;
      }
    }
    else if ((iRam00000000020e8104 != 0x10) && (iRam00000000020e8104 != 0x40)) {
      if (((bRam000000000211be68 & 1) == 0) && (iRam00000000020eccc8 == 1)) {
        if ((bRam000000000211be69 & 1) == 0) {
          FUN_020685c0(iRam000000000011df04 + 1,0x20c1e6f,extraout_x1_00,_uRam000000000011df10,
                       uRam000000000011df6c);
          iRam000000000011df04 = extraout_w8;
          *(undefined1 *)(extraout_x9 + 0xe69) = extraout_w10;
          FUN_0208ada0();
          cRam000000000211be35 = '\0';
          uVar8 = FUN_02067cb4();
          if (((uVar8 & 1) != 0) && ((_uRam000000000011df10 >> 0xb & 1) != 0)) {
            _uRam000000000011df10 = _uRam000000000011df10 & 0xffffe7ff | 0x1000;
            return;
          }
          _uRam000000000011df10 = _uRam000000000011df10 | 0x800;
        }
        uVar8 = (ulong)uRam000000000011df6c;
        if (uRam000000000011df6c < 0x17000) {
          uRam000000000011df6c = uRam000000000011df6c + 1;
          *(undefined1 *)(*plVar3 + uVar8) = uVar2;
        }
        else if ((uRam000000000011df10._1_1_ >> 4 & 1) == 0) {
          iVar5 = 0;
          if (iRam000000000211be6c + 1U < 0x800) {
            iVar5 = iRam000000000211be6c + 1;
          }
          *(undefined1 *)(*plVar3 + (ulong)(iRam000000000211be6c + 0x16800)) = uVar2;
          iRam000000000211be6c = iVar5;
        }
      }
      else {
        if ((bRam000000000211be69 & 1) == 0) {
          bRam000000000211be69 = 1;
          FUN_02067cb4();
          iRam000000000011df04 = iRam000000000011df04 + 1;
        }
        if (iRam00000000020e8104 == 0x100) {
          FUN_02067af4();
          FUN_02067c00();
          FUN_02067974(1);
          FUN_020675ec();
          iRam000000000011df0c = 0x5678ef90;
          cRam000000000211be35 = 1;
          lRam000000000211be50 =
               FUN_0206b7f0(0x40000,0x10000,0xffffffffffffffff,0,1,0x20b6981,in_x6,in_x7,unaff_x29,
                            unaff_x30);
          cRam000000000211be35 = 0;
          if (lRam000000000211be50 == 0) {
            FUN_020685c0(0x20cb498);
            cRam000000000211be35 = 1;
            FUN_0208ada0();
            _uRam000000000011df10 = 0x40;
            iRam00000000020e8104 = 0x40;
          }
          else {
            FUN_0200329c(uRam0000000002103328,lRam000000000211be50,lRam000000000211be50,0x40,5);
            FUN_02091664(lRam000000000211be50,0,0x40000);
            uRam000000000011df14 = (undefined4)lRam000000000211be50;
            uRam000000000011df18 = (undefined4)((ulong)lRam000000000211be50 >> 0x20);
            uRam000000000011df20 = 0x20;
            uRam000000000011df1c = 0x40000;
            _uRam000000000011df10 = 0x27;
            uRam000000000011df7c = 0;
            FUN_02066cb8();
            uVar7 = 0xdf00;
            iRam00000000020e8104 = 0x200;
            auVar9 = FUN_020685c0(uRam000000000011df18,0x20bac10,extraout_x1,0xdf00,
                                  uRam000000000011df00,0x11df0c);
            cRam000000000211be35 = 1;
            FUN_0208ada0(auVar9._0_8_,auVar9._8_8_,uVar7 & 0xffff | 0x110000);
          }
          cRam000000000211be35 = 0;
          return;
        }
        iVar5 = FUN_02066d14();
        if ((iVar5 != 0) && (iRam00000000020e8104 == 0x200)) {
          *(undefined1 *)
           (lRam000000000211be50 + (ulong)uRam000000000011df48 + (ulong)uRam000000000011df4c) =
               uVar2;
          uRam000000000011df4c = uRam000000000011df4c + 1;
          uRam000000000011df20 = CONCAT44(uRam000000000011df4c,(undefined4)uRam000000000011df20);
          if (0x3ffff < uRam000000000011df4c + uRam000000000011df48) {
            FUN_020685c0(0x10,0x20ce655);
            iRam00000000020e8104 = extraout_w8_00;
            FUN_0208ada0();
            cRam000000000211be35 = '\0';
          }
        }
      }
    }
    return;
  }
  return;
}


