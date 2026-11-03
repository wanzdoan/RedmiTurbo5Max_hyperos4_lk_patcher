// File offset entry: 0x67db4

void FUN_02067cb4(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  
  FUN_020685c0(0x20c2ce7,param_2,iRam000000000011df00);
  uRam000000000211be35 = 1;
  FUN_0208ada0();
  iVar1 = iRam000000000011df00;
  uRam000000000211be35 = 0;
  if (iRam000000000011df00 != 0x24013186) {
    FUN_02091664(0x11df00,0,0x100);
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20c5f5e,0x20b58bc);
    iRam000000000011df00 = 0x24013186;
    uRam000000000011df80 = 0x76657231;
    uRam000000000011df0c = 0x5678ef90;
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20c401b,0x20b58bc,0x24013186);
  }
  uRam000000000211be35 = 0;
  uRam000000000211be58 = iVar1 == 0x24013186;
  return;
}


