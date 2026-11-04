
/* WARNING: Unknown calling convention -- yet parameter storage is locked */

uint sleep(uint __seconds)

{
  uint uVar1;

  uVar1 = (*(code *)PTR_sleep_0042a5c0)(__seconds);
  return uVar1;
}
