// File offset entry: 0x63604

/* WARNING: Possible PIC construction at 0x02063580: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020635fc: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0206379c: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020638f8: Changing call to branch */
/* WARNING: Possible PIC construction at 0x0206384c: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020638fc) */
/* WARNING: Removing unreachable block (ram,0x02063918) */
/* WARNING: Removing unreachable block (ram,0x02063900) */
/* WARNING: Removing unreachable block (ram,0x020637a0) */
/* WARNING: Removing unreachable block (ram,0x020637b8) */
/* WARNING: Removing unreachable block (ram,0x0206380c) */
/* WARNING: Removing unreachable block (ram,0x02063820) */
/* WARNING: Removing unreachable block (ram,0x020638f4) */
/* WARNING: Removing unreachable block (ram,0x02063830) */
/* WARNING: Removing unreachable block (ram,0x02063870) */
/* WARNING: Removing unreachable block (ram,0x0206387c) */
/* WARNING: Removing unreachable block (ram,0x02063834) */
/* WARNING: Removing unreachable block (ram,0x02063884) */
/* WARNING: Removing unreachable block (ram,0x020638c8) */
/* WARNING: Removing unreachable block (ram,0x02063890) */
/* WARNING: Removing unreachable block (ram,0x02063898) */
/* WARNING: Removing unreachable block (ram,0x0206383c) */
/* WARNING: Removing unreachable block (ram,0x02063844) */
/* WARNING: Removing unreachable block (ram,0x02063800) */
/* WARNING: Removing unreachable block (ram,0x020637a4) */
/* WARNING: Removing unreachable block (ram,0x02063600) */
/* WARNING: Removing unreachable block (ram,0x02063614) */
/* WARNING: Removing unreachable block (ram,0x0206361c) */
/* WARNING: Removing unreachable block (ram,0x02063660) */
/* WARNING: Removing unreachable block (ram,0x02063678) */
/* WARNING: Removing unreachable block (ram,0x02063684) */
/* WARNING: Removing unreachable block (ram,0x0206368c) */
/* WARNING: Removing unreachable block (ram,0x02063724) */
/* WARNING: Removing unreachable block (ram,0x02063730) */
/* WARNING: Removing unreachable block (ram,0x02063708) */
/* WARNING: Removing unreachable block (ram,0x02063714) */
/* WARNING: Removing unreachable block (ram,0x020636ec) */
/* WARNING: Removing unreachable block (ram,0x020636dc) */
/* WARNING: Removing unreachable block (ram,0x0206373c) */
/* WARNING: Removing unreachable block (ram,0x020636e4) */
/* WARNING: Removing unreachable block (ram,0x020636f8) */
/* WARNING: Removing unreachable block (ram,0x020636ac) */
/* WARNING: Removing unreachable block (ram,0x02063744) */
/* WARNING: Removing unreachable block (ram,0x0206374c) */
/* WARNING: Removing unreachable block (ram,0x020636bc) */
/* WARNING: Removing unreachable block (ram,0x0206376c) */
/* WARNING: Removing unreachable block (ram,0x02063654) */
/* WARNING: Removing unreachable block (ram,0x0206379c) */
/* WARNING: Removing unreachable block (ram,0x02063604) */
/* WARNING: Removing unreachable block (ram,0x02063584) */
/* WARNING: Removing unreachable block (ram,0x0206359c) */
/* WARNING: Removing unreachable block (ram,0x020635dc) */
/* WARNING: Removing unreachable block (ram,0x020635d4) */
/* WARNING: Removing unreachable block (ram,0x020635fc) */
/* WARNING: Removing unreachable block (ram,0x02063588) */
/* WARNING: Removing unreachable block (ram,0x02063850) */
/* WARNING: Removing unreachable block (ram,0x02063864) */
/* WARNING: Removing unreachable block (ram,0x020638d0) */
/* WARNING: Removing unreachable block (ram,0x020638d4) */
/* WARNING: Removing unreachable block (ram,0x020638f8) */
/* WARNING: Recovered jumptable eliminated as dead code */

undefined8 FUN_02063504(undefined4 param_1,uint param_2)

{
  bool bVar1;
  bool bVar2;
  undefined8 uVar3;
  uint extraout_w8;
  undefined4 uStack_2c;
  undefined8 uStack_28;
  
  uStack_28 = *puRam0000000002103280;
  uStack_2c = param_1;
  func_0x0206333c(&uStack_2c);
  bVar1 = 6 < param_2;
  bVar2 = param_2 == 7;
  uVar3 = 0xfffffffe;
  if ((param_2 < 8) && (uVar3 = func_0x0206393c(0xfffffffe), !bVar1 || bVar2)) {
    uVar3 = 0;
    *(uint *)(((ulong)(extraout_w8 << 1) & 0x7ffffff0) + 0x1002d30c) =
         1 << (ulong)((extraout_w8 & 7) << 2 | 3) | param_2 << (ulong)((extraout_w8 & 7) << 2);
  }
  return uVar3;
}


