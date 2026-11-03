// File offset entry: 0x99f98

/* WARNING: Removing unreachable block (ram,0x02099f14) */

void FUN_02099e98(char param_1)

{
  uint uVar1;
  int iVar2;
  uint uVar3;
  
  uVar3 = uRam000000001c010024;
  uRam000000001c010020 = uRam000000001c01000c;
  FUN_02099c6c();
  uRam000000001c010008 = 0x1971;
  uRam0000000002121ff0 = 1;
  uRam000000001c010048 = 0x90000000;
  iVar2 = func_0x0207bbc4(0);
  if (iVar2 < 0) {
    FUN_0208ada0(0x20c88f5);
  }
  uVar1 = uRam000000001c010000 & 0xffffffa7;
  func_0x02099be0(0);
  if (param_1 == '\0') {
    FUN_0208ada0(0x20b92d1);
    func_0x02099bcc();
  }
  else {
    uVar3 = uVar3 | 0x2000;
  }
  uRam000000001c010000 = uVar1 | 0x22000004;
  uRam000000001c010024 = uVar3;
  FUN_0208ada0(0x20ccce0,uRam000000001c010000);
  FUN_0208440c();
  FUN_02091c94(100);
  uRam000000001c010014 = 0x1209;
  return;
}


