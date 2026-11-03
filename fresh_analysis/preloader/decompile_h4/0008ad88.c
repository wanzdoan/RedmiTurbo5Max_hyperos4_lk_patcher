// File offset entry: 0x8ad88

/* WARNING: Removing unreachable block (ram,0x02068200) */

void FUN_0208ac88(undefined4 param_1)

{
  undefined1 uVar1;
  long *plVar2;
  undefined1 *puVar3;
  int iVar4;
  undefined8 extraout_x1;
  undefined8 extraout_x1_00;
  uint uVar5;
  undefined8 in_x6;
  undefined8 in_x7;
  int extraout_w8;
  int extraout_w8_00;
  ulong uVar6;
  long extraout_x9;
  undefined1 extraout_w10;
  undefined8 unaff_x29;
  undefined8 unaff_x30;
  undefined1 auVar7 [16];
  
  uVar1 = (undefined1)param_1;
  if (iRam000000000211d9f0 == 0) {
    if ((iRam00000000020eccd0 != 0) && (iVar4 = func_0x0200c72c(), iVar4 == 0)) {
      func_0x020924c8(param_1);
    }
  }
  else if (puRam000000000211da00 < puRam000000000211da08) {
    puVar3 = puRam000000000211da00 + 1;
    *puRam000000000211da00 = uVar1;
    puRam000000000211da00 = puVar3;
  }
  else {
    iRam000000000211da10 = iRam000000000211da10 + 1;
  }
  func_0x0200c140(param_1);
  plVar2 = plRam00000000021036f0;
  if (iRam00000000020ecccc != 0) {
    if (cRam000000000211be35 == '\x01') {
      if ((((iRam000000000011df0c == 0x5678ef90) && ((bRam000000000211be68 & 1) == 0)) &&
          (iRam00000000020eccc8 == 1)) &&
         (uVar6 = (ulong)uRam000000000011df6c, uRam000000000011df6c < 0x17000)) {
        uRam000000000011df6c = uRam000000000011df6c + 1;
        *(undefined1 *)(*plRam00000000021036f0 + uVar6) = uVar1;
      }
    }
    else if ((iRam00000000020e8104 != 0x10) && (iRam00000000020e8104 != 0x40)) {
      if (((bRam000000000211be68 & 1) == 0) && (iRam00000000020eccc8 == 1)) {
        if ((bRam000000000211be69 & 1) == 0) {
          func_0x020685c0(iRam000000000011df04 + 1,0x20c1e6f,extraout_x1_00,_uRam000000000011df10,
                          uRam000000000011df6c);
          iRam000000000011df04 = extraout_w8;
          *(undefined1 *)(extraout_x9 + 0xe69) = extraout_w10;
          FUN_0208ada0();
          cRam000000000211be35 = '\0';
          uVar6 = func_0x02067cb4();
          if (((uVar6 & 1) != 0) && ((_uRam000000000011df10 >> 0xb & 1) != 0)) {
            _uRam000000000011df10 = _uRam000000000011df10 & 0xffffe7ff | 0x1000;
            return;
          }
          _uRam000000000011df10 = _uRam000000000011df10 | 0x800;
        }
        uVar6 = (ulong)uRam000000000011df6c;
        if (uRam000000000011df6c < 0x17000) {
          uRam000000000011df6c = uRam000000000011df6c + 1;
          *(undefined1 *)(*plVar2 + uVar6) = uVar1;
        }
        else if ((uRam000000000011df10._1_1_ >> 4 & 1) == 0) {
          iVar4 = 0;
          if (iRam000000000211be6c + 1U < 0x800) {
            iVar4 = iRam000000000211be6c + 1;
          }
          *(undefined1 *)(*plVar2 + (ulong)(iRam000000000211be6c + 0x16800)) = uVar1;
          iRam000000000211be6c = iVar4;
        }
      }
      else {
        if ((bRam000000000211be69 & 1) == 0) {
          bRam000000000211be69 = 1;
          func_0x02067cb4();
          iRam000000000011df04 = iRam000000000011df04 + 1;
        }
        if (iRam00000000020e8104 == 0x100) {
          func_0x02067af4();
          func_0x02067c00();
          func_0x02067974(1);
          func_0x020675ec();
          iRam000000000011df0c = 0x5678ef90;
          cRam000000000211be35 = 1;
          lRam000000000211be50 =
               func_0x0206b7f0(0x40000,0x10000,0xffffffffffffffff,0,1,0x20b6981,in_x6,in_x7,
                               unaff_x29,unaff_x30);
          cRam000000000211be35 = 0;
          if (lRam000000000211be50 == 0) {
            func_0x020685c0(0x20cb498);
            cRam000000000211be35 = 1;
            FUN_0208ada0();
            _uRam000000000011df10 = 0x40;
            iRam00000000020e8104 = 0x40;
          }
          else {
            func_0x0200329c(uRam0000000002103328,lRam000000000211be50,lRam000000000211be50,0x40,5);
            FUN_02091664(lRam000000000211be50,0,0x40000);
            uRam000000000011df14 = (undefined4)lRam000000000211be50;
            uRam000000000011df18 = (undefined4)((ulong)lRam000000000211be50 >> 0x20);
            uRam000000000011df20 = 0x20;
            uRam000000000011df1c = 0x40000;
            _uRam000000000011df10 = 0x27;
            uRam000000000011df7c = 0;
            func_0x02066cb8();
            uVar5 = 0xdf00;
            iRam00000000020e8104 = 0x200;
            auVar7 = func_0x020685c0(uRam000000000011df18,0x20bac10,extraout_x1,0xdf00,
                                     uRam000000000011df00,0x11df0c);
            cRam000000000211be35 = 1;
            FUN_0208ada0(auVar7._0_8_,auVar7._8_8_,uVar5 & 0xffff | 0x110000);
          }
          cRam000000000211be35 = 0;
          return;
        }
        iVar4 = func_0x02066d14();
        if ((iVar4 != 0) && (iRam00000000020e8104 == 0x200)) {
          *(undefined1 *)
           (lRam000000000211be50 + (ulong)uRam000000000011df48 + (ulong)uRam000000000011df4c) =
               uVar1;
          uRam000000000011df4c = uRam000000000011df4c + 1;
          uRam000000000011df20 = CONCAT44(uRam000000000011df4c,(undefined4)uRam000000000011df20);
          if (0x3ffff < uRam000000000011df4c + uRam000000000011df48) {
            func_0x020685c0(0x10,0x20ce655);
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


