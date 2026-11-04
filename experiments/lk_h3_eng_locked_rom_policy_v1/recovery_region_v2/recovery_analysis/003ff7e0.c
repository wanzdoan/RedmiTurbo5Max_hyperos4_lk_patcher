
/* Paths::Get() */

undefined * Paths::Get(void)

{
  int iVar1;

  if (((DAT_0057d718 & 1) == 0) && (iVar1 = __cxa_guard_acquire(&DAT_0057d718), iVar1 != 0)) {
    Paths((Paths *)&DAT_0057d658);
    __cxa_atexit(~Paths,&DAT_0057d658,&DAT_0042ce00);
    __cxa_guard_release(&DAT_0057d718);
  }
  return &DAT_0057d658;
}
